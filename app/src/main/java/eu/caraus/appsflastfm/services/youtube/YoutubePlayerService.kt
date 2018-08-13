package eu.caraus.appsflastfm.services.youtube

import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager

import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.commit451.youtubeextractor.YouTubeExtraction
import com.commit451.youtubeextractor.YouTubeExtractor

//import com.facebook.network.connectionclass.ConnectionQuality
//import com.facebook.network.connectionclass.DeviceBandwidthSampler

import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.extensions.subOnIoObsOnUi
import eu.caraus.appsflastfm.common.notification.NotificationHandler
import eu.caraus.appsflastfm.common.schedulers.SchedulerProvider
import eu.caraus.appsflastfm.data.domain.extensions.lastFm.TrackState
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionSeek
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate

import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeMediaType
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.base.BaseService

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit
import javax.inject.Inject


class YoutubePlayerService : BaseService(),
        MediaPlayer.OnCompletionListener ,
        MediaPlayer.OnPreparedListener   {

    companion object {

        const val ACTION_PLAY = "ACTION_PLAY"

        const val ACTION_STOP = "ACTION_STOP"

        const val ACTION_PAUSE = "ACTION_PAUSE"

    }


    private val mediaPlayer      : MediaPlayer = MediaPlayer()
    private var mediaController  : MediaControllerCompat? = null
    private var mediaSession     : MediaSessionCompat? = null

    private var mediaType = YouTubeMediaType.YOUTUBE_MEDIA_NONE

    private var youTubeVideo : YouTubeVideo? = null

    private var notificationHandler = NotificationHandler(this)

    private var elapsedDisposable : Disposable? = null
    private var playingDisposable  : Disposable? = null

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

        initMediaSessions()

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

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */

    override fun onPrepared( mp : MediaPlayer ) {

    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    override fun onCompletion( mPlayer : MediaPlayer) {

    }

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
                video.thumbnailURL = it?.thumbnails?.get(0)?.url
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

    private fun getPendingIntent() : PendingIntent
            = PendingIntent.getBroadcast(
            this, 0, Intent(Intent.ACTION_MEDIA_BUTTON),
                        PendingIntent.FLAG_CANCEL_CURRENT)

    private fun initMediaSessions() {

        mediaPlayer.setWakeMode( this , PowerManager.PARTIAL_WAKE_LOCK )

        mediaSession = MediaSessionCompat( this, "SESSION", null, getPendingIntent())

        mediaSession?.let {

            mediaController = MediaControllerCompat(this, it.sessionToken )

            it.setCallback( object : MediaSessionCompat.Callback(){
                override fun onPlay() {
                    super.onPlay()
                    resumePlayback()

                }
                override fun onPause() {
                    super.onPause()
                    pausePlayback()

                }
                override fun onStop() {
                    super.onStop()
                    stopPlayback()

                }
            })
        }

    }

    private fun pausePlayback() {
        mediaPlayer.let {
            if( it.isPlaying ) it.pause()
        }
    }

    private fun resumePlayback() {
        mediaPlayer.let {
            if( !it.isPlaying ) it.start()
        }
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

        if( youtubeUrl?.videoStreams?.isNotEmpty() == true )
        youtubeUrl.videoStreams.get(0).url.let { url ->
            mediaPlayer.let {
                it.reset()
                it.setDataSource( url)
                it.setVolume(1.0f,1.0f)
                it.setAudioAttributes( audioAttributes())
                it.prepare()
                it.start()
            }
        }
    }

    private fun playError( error : Throwable ){
        stopPlayback()
    }

    private fun audioAttributes() : AudioAttributes {
        return AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
    }

}
