package eu.caraus.appsflastfm.services.youtube


import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder

import com.commit451.youtubeextractor.YouTubeExtraction
import com.commit451.youtubeextractor.YouTubeExtractor

import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.ui.common.notification.NotificationsUtil
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionSeek
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.ErrorUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate

import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.base.BaseService

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

import java.util.concurrent.TimeUnit
import javax.inject.Inject


class YoutubePlayerService : BaseService(),
        MediaPlayer.OnCompletionListener ,
        MediaPlayer.OnPreparedListener   {

    companion object {

        const val ACTION_PLAY = "ACTION_PLAY"

        const val ACTION_STOP = "ACTION_STOP"

    }

    private val mediaPlayer      : MediaPlayer = MediaPlayer()

    private var youTubeVideo : YouTubeVideo? = null

    private var notificationHandler = NotificationsUtil(this)

    private var elapsedDisposable : Disposable? = null

    @Inject
    lateinit var schedulers : SchedulerProvider

    @Inject
    lateinit var rxBus : RxBus

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnPreparedListener(this)

        initBus()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleIntent( intent: Intent? ){
        intent?.let {
            when( it.action ){
                ACTION_STOP -> actionStop()
            }
        }
    }

    override fun onTaskRemoved( rootIntent : Intent ) {
        notificationHandler.cancel()
    }

    override fun onPrepared( mp : MediaPlayer ) { }

    override fun onCompletion( mPlayer : MediaPlayer) { }

    private fun initBus() {
        rxBus.client().subscribe {
            when( it ){
                is ActionPlay -> actionPlay( it.youTubeVideo )
                is ActionStop -> actionStop( )
                is ActionSeek -> actionSeek( it.seekTo )
            }
        }
    }

    private fun actionPlay( youTubeVideo : YouTubeVideo ){
        this.youTubeVideo = youTubeVideo
        this.youTubeVideo?.let { video ->
            playYouTubeUrl( video , {
                playAudio(it)
                actionPlayUpdate()
            }, {
                playError(it)
                actionStop()
            })
        }
    }

    private fun actionStop( ){
        this.youTubeVideo?.let {
            stopPlayback()
            actionStopUpdate()
        }
        this.youTubeVideo = null
    }

    private fun actionSeek( seekTo : Int ){
        this.youTubeVideo?.let {
            seekToPosition( seekTo )
            actionElapsedUpdate()
        }
    }

    private fun actionPlayUpdate(){

        youTubeVideo?.let {

            it.trackState   = TrackState.PLAYING
            it.trackElapsed = 0

            rxBus.sendEventToClient( PlayingUpdate( it ))

            elapsedUpdateTimerStart()

            notificationHandler.buildNotification( ACTION_STOP, it )
        }
    }

    private fun actionStopUpdate(){
        youTubeVideo?.let {

            it.trackState = TrackState.STOPPED
            it.trackElapsed = 0

            rxBus.sendEventToClient( StoppedUpdate( it ))

            elapsedUpdateTimerStop()

            notificationHandler.cancel()
        }
    }

    private fun actionElapsedUpdate(){

        youTubeVideo?.let {

            it.trackState = TrackState.PLAYING
            it.trackElapsed = ( mediaPlayer.currentPosition / 1000f ).toInt()

            rxBus.sendEventToClient( ElapsedUpdate(it))

            notificationHandler.buildNotification( ACTION_STOP, it )
        }

    }

    private fun elapsedUpdateTimerStart(){
        elapsedDisposable?.dispose()
        elapsedDisposable =
                Observable.interval(1,TimeUnit.SECONDS).timeInterval()
                          .subOnIoObsOnUi(schedulers)
                          .subscribe({ _->
                                actionElapsedUpdate()
                            },{
                                playError(it)
                          })
    }

    private fun elapsedUpdateTimerStop(){
        elapsedDisposable?.dispose()
    }

    private fun stopPlayback(){
        mediaPlayer.let {
            it.stop()
            it.reset()
        }
    }

    private fun seekToPosition( seekTo : Int ) {
        mediaPlayer.let {
            it.seekTo( seekTo * 1000)
        }
    }

    private fun playYouTubeUrl( youTubeVideo : YouTubeVideo,
                                success: ( url : YouTubeExtraction? ) -> Unit,
                                error: ( error : Throwable )-> Unit ) {
        youTubeVideo.id?.let { videoId ->
            YouTubeExtractor.Builder().build()
                    .extract( videoId )
                    .subscribeOn( schedulers.io())
                    .observeOn( schedulers.ui())
                    .subscribe({
                            success(it)
                        },{
                            error(it)
                    })

        }
    }

    private fun playAudio( youtubeUrl : YouTubeExtraction?) {

        if( youtubeUrl?.videoStreams?.isNotEmpty() == true ){
            youtubeUrl.videoStreams[0].url.let { url ->
                mediaPlayer.let {
                    it.reset()
                    it.setDataSource( url)
                    it.setVolume(1.0f,1.0f)
                    it.setAudioAttributes( audioAttributes())
                    it.prepare()
                    it.start()
                }
            }
        } else {
            playError( Throwable("Can't play, empty streams") )
        }
    }

    private fun playError( error : Throwable ){
        stopPlayback()
        playErrorUpdate( error.localizedMessage )
    }

    private fun playErrorUpdate( error : String ){
        rxBus.sendEventToClient( ErrorUpdate(error) )
    }

    private fun audioAttributes() : AudioAttributes {
        return AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
    }

}
