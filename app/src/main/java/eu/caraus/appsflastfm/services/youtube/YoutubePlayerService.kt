package eu.caraus.appsflastfm.services.youtube

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import android.os.RemoteException

import android.support.v4.media.RatingCompat

import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.SparseArray

import com.facebook.network.connectionclass.ConnectionClassManager
import com.facebook.network.connectionclass.ConnectionQuality
import com.facebook.network.connectionclass.DeviceBandwidthSampler

import java.io.IOException
import java.util.ArrayList

import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import eu.caraus.appsflastfm.common.bus.RxBus
import eu.caraus.appsflastfm.common.notification.NotificationHandler
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionPlay
import eu.caraus.appsflastfm.services.youtube.busevents.client.ActionStop
import eu.caraus.appsflastfm.services.youtube.busevents.service.ElapsedUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.PlayingUpdate
import eu.caraus.appsflastfm.services.youtube.busevents.service.StoppedUpdate

import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeMediaType
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeConfig
import eu.caraus.appsflastfm.ui.base.BaseService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class YoutubePlayerService : BaseService(),
        MediaPlayer.OnCompletionListener ,
        MediaPlayer.OnPreparedListener   {


    private val mMediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var mSession: MediaSessionCompat
    private var mController: MediaControllerCompat? = null

    private var mediaType = YouTubeMediaType.YOUTUBE_MEDIA_NONE

    private var youTubeVideo: YouTubeVideo? = null

    private var isStarting = false
    private var currentSongIndex = 0

    private var youTubeVideos: ArrayList<YouTubeVideo>? = null

    private var notificationHandler : NotificationHandler? = null

    private var deviceBandwidthSampler: DeviceBandwidthSampler? = null
    private var connectionQuality: ConnectionQuality? = ConnectionQuality.MODERATE

    @Inject
    lateinit var rxBus : RxBus

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {

        super.onCreate()

        youTubeVideo = YouTubeVideo()

        mMediaPlayer.setOnCompletionListener(this)
        mMediaPlayer.setOnPreparedListener(this)

        initMediaSessions()
        initPhoneCallListener()

        deviceBandwidthSampler = DeviceBandwidthSampler.getInstance()
        notificationHandler    = NotificationHandler(this )

        initBus()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initPhoneCallListener() {
        val phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: ActionPause music
                    pauseVideo()
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: ActionUpdate music
                    resumeVideo()
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }

        val mgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }


    private fun handleIntent(intent: Intent?) {
        if (intent == null || intent.action == null)
            return
        val action = intent.action
        if (action!!.equals(ACTION_PLAY, ignoreCase = true)) {
            handleMedia(intent)
            mController!!.transportControls.play()
        } else if (action.equals(ACTION_PAUSE, ignoreCase = true)) {
            mController!!.transportControls.pause()
        } else if (action.equals(ACTION_PREVIOUS, ignoreCase = true)) {
            mController!!.transportControls.skipToPrevious()
        } else if (action.equals(ACTION_NEXT, ignoreCase = true)) {
            mController!!.transportControls.skipToNext()
        } else if (action.equals(ACTION_STOP, ignoreCase = true)) {
            mController!!.transportControls.stop()
        }
    }


    private fun handleMedia(intent: Intent) {
        var intentMediaType = YouTubeMediaType.YOUTUBE_MEDIA_NONE
        if (intent.getSerializableExtra(YouTubeConfig.YOUTUBE_TYPE) != null) {
            intentMediaType = intent.getSerializableExtra(YouTubeConfig.YOUTUBE_TYPE) as YouTubeMediaType
        }
        when (intentMediaType) {
            YouTubeMediaType.YOUTUBE_MEDIA_NONE //video is paused,so no new playback requests should be processed
            -> mMediaPlayer!!.start()
            YouTubeMediaType.YOUTUBE_MEDIA_TYPE_VIDEO -> {
                mediaType = YouTubeMediaType.YOUTUBE_MEDIA_TYPE_VIDEO
                youTubeVideo = intent.getSerializableExtra(YouTubeConfig.YOUTUBE_TYPE_VIDEO) as YouTubeVideo
                if (youTubeVideo!!.id != null) {
                    playVideo()
                }
            }
            YouTubeMediaType.YOUTUBE_MEDIA_TYPE_PLAYLIST //new playlist playback request
            -> {
                mediaType = YouTubeMediaType.YOUTUBE_MEDIA_TYPE_PLAYLIST
                youTubeVideos = intent.getSerializableExtra(YouTubeConfig.YOUTUBE_TYPE_PLAYLIST) as ArrayList<YouTubeVideo>
                val startPosition = intent.getIntExtra(YouTubeConfig.YOUTUBE_TYPE_PLAYLIST_VIDEO_POS, 0)
                youTubeVideo = youTubeVideos!![startPosition]
                currentSongIndex = startPosition
                playVideo()
            }
        }
    }

    private fun initBus() {
        rxBus.client().subscribe {
            when( it ){
                is ActionPlay -> toggle( it.youTubeVideo )
                is ActionStop -> toggle( this.youTubeVideo!!)
            }
        }
    }

    private fun toggle( youTubeVideo : YouTubeVideo ){

        this.youTubeVideo = youTubeVideo

        when( !mMediaPlayer.isPlaying ){
            true  ->
                play( youTubeVideo  )
            false ->
                stop( youTubeVideo )
        }
    }

    var disposable : Disposable? = null

    private fun play( youTubeVideo: YouTubeVideo ){

        extractUrlAndPlay( youTubeVideo )
        rxBus.sendEventToClient( PlayingUpdate( youTubeVideo))

        disposable?.dispose()
        disposable = Observable.interval(1, TimeUnit.SECONDS).timeInterval()
                  .subscribeOn( Schedulers.io())
                  .observeOn( AndroidSchedulers.mainThread())
                  .subscribe { _->
                      mMediaPlayer.let {
                          rxBus.sendEventToClient(
                                  ElapsedUpdate( this.youTubeVideo!! ,mMediaPlayer.currentPosition / 1000 ) )
                      }
                  }
    }

    private fun stop(youTubeVideo: YouTubeVideo){
        disposable?.dispose()
        stopPlayer()
        rxBus.sendEventToClient( StoppedUpdate( youTubeVideo))
    }

    /**
     * Initializes media sessions and receives media events
     */
    private fun initMediaSessions() {

        mMediaPlayer.setWakeMode( this , PowerManager.PARTIAL_WAKE_LOCK)

        val buttonReceiverIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(Intent.ACTION_MEDIA_BUTTON),
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        mSession = MediaSessionCompat( this, "simple player session", null, buttonReceiverIntent)

        try {

            mController = MediaControllerCompat(applicationContext, mSession!!.sessionToken)

            mSession!!.setCallback(
                    object : MediaSessionCompat.Callback() {
                        override fun onPlay() {
                            super.onPlay()

                            generateNotification(ACTION_PAUSE)


                        }

                        override fun onPause() {

                            super.onPause()
                            pauseVideo()
                            generateNotification(ACTION_PLAY)
                        }

                        override fun onSkipToNext() {
                            super.onSkipToNext()
                            if (!isStarting) {
                                playNext()
                            }
                            generateNotification(ACTION_PAUSE)
                        }

                        override fun onSkipToPrevious() {
                            super.onSkipToPrevious()
                            if (!isStarting) {
                                playPrevious()
                            }
                            generateNotification(ACTION_PLAY)
                        }

                        override fun onStop() {
                            super.onStop()
                            stopPlayer()
                            //remove notification and stop service
                            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.cancel(1)
                            val intent = Intent(applicationContext, YoutubePlayerService::class.java)
                            stopService(intent)
                        }

                        override fun onSetRating(rating: RatingCompat?) {
                            super.onSetRating(rating)
                        }
                    }
            )
        } catch (re: RemoteException) {
            re.printStackTrace()
        }

    }


    private fun playNext() {
        //if media type is video not playlist, just loop it
        if (mediaType === YouTubeMediaType.YOUTUBE_MEDIA_TYPE_VIDEO) {
            seekVideo(0)
            restartVideo()
            return
        }

        if (youTubeVideos!!.size > currentSongIndex + 1) {
            currentSongIndex++
        } else { //play 1st song
            currentSongIndex = 0
        }

        youTubeVideo = youTubeVideos!![currentSongIndex]
        playVideo()
    }


    private fun playPrevious() {
        //if media type is video not playlist, just loop it
        if (mediaType === YouTubeMediaType.YOUTUBE_MEDIA_TYPE_VIDEO) {
            restartVideo()
            return
        }

        if (currentSongIndex - 1 >= 0) {
            currentSongIndex--
        } else { //play last song
            currentSongIndex = youTubeVideos!!.size - 1
        }
        youTubeVideo = youTubeVideos!![youTubeVideos!!.size - 1]
        playVideo()
    }


    private fun playVideo() {
        isStarting = true
        //extractUrlAndPlay()
    }


    private fun pauseVideo() {
        mMediaPlayer.let {
            if(mMediaPlayer.isPlaying) mMediaPlayer.pause()
        }
    }

    private fun resumeVideo() {
        mMediaPlayer.let {
            if(!mMediaPlayer.isPlaying ) mMediaPlayer.start()
        }
    }

    private fun restartVideo() {
        mMediaPlayer.start()
    }


    private fun seekVideo(seekTo: Int) {
        mMediaPlayer.seekTo(seekTo)
    }


    private fun stopPlayer() {
        mMediaPlayer.stop()
        mMediaPlayer.reset()
    }

    /**
     * Get the best available audio stream
     *
     *
     * Itags:
     * 141 - mp4a - stereo, 44.1 KHz 256 Kbps
     * 251 - webm - stereo, 48 KHz 160 Kbps
     * 140 - mp4a - stereo, 44.1 KHz 128 Kbps
     * 17  - mp4 - stereo, 44.1 KHz 96-100 Kbps
     *
     * @param ytFiles Array of available streams
     * @return Audio stream with highest bitrate
     */
    private fun getBestStream(ytFiles: SparseArray<YtFile>?): YtFile {

        connectionQuality = ConnectionClassManager.getInstance().currentBandwidthQuality
        var itags = intArrayOf(251, 141, 140, 17)

        if (connectionQuality != null && connectionQuality != ConnectionQuality.UNKNOWN) {
            when (connectionQuality) {
                ConnectionQuality.POOR -> itags = intArrayOf(17, 140, 251, 141)
                ConnectionQuality.MODERATE -> itags = intArrayOf(251, 141, 140, 17)
                ConnectionQuality.GOOD, ConnectionQuality.EXCELLENT -> itags = intArrayOf(141, 251, 140, 17)
            }
        }

        if (ytFiles!!.get(itags[0]) != null) {
            return ytFiles.get(itags[0])
        } else if (ytFiles.get(itags[1]) != null) {
            return ytFiles.get(itags[1])
        } else if (ytFiles.get(itags[2]) != null) {
            return ytFiles.get(itags[2])
        }
        return ytFiles.get(itags[3])
    }

    /**
     * Extracts link from youtube video ID, so mediaPlayer can play it
     */
    private fun extractUrlAndPlay( youTubeVideo: YouTubeVideo ) {

        val youtubeLink = YouTubeConfig.YOUTUBE_BASE_URL + youTubeVideo.id

        deviceBandwidthSampler?.startSampling()

        youTubeExtractor = object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta) {
                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    //                    Toast.makeText(YTApplication.getAppContext(), R.string.failed_playback,
                    //                            Toast.LENGTH_SHORT).show();
                    return
                }
                deviceBandwidthSampler!!.stopSampling()
                val ytFile = getBestStream(ytFiles)
                try {
                    if (mMediaPlayer != null) {
                        mMediaPlayer!!.reset()
                        mMediaPlayer!!.setDataSource(ytFile.url)
                        mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        mMediaPlayer!!.prepare()
                        mMediaPlayer!!.start()

                        //Toast.makeText(YTApplication.getAppContext(), youTubeVideo.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                } catch (io: IOException) {
                    io.printStackTrace()
                }

            }
        }

        youTubeExtractor?.execute( youtubeLink )

    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(1)
    }

    override fun onCompletion(_mediaPlayer: MediaPlayer) {

        if ( mediaType === YouTubeMediaType.YOUTUBE_MEDIA_TYPE_PLAYLIST) {

            playNext()

            notificationHandler?.apply {
                this.buildNotification(
                        this.generateAction( android.R.drawable.ic_media_pause,"ActionPause", ACTION_PAUSE),
                        youTubeVideo!!
                )
            }


        } else {
            restartVideo()
        }
    }

    private fun generateNotification( action : String ){
        when( action ){

            ACTION_PAUSE ->
                notificationHandler?.apply {
                    this.buildNotification(
                            this.generateAction( android.R.drawable.ic_media_pause,"ActionPause", ACTION_PAUSE),
                            youTubeVideo!! )
                }

            ACTION_PLAY ->
                notificationHandler?.apply {
                    this.buildNotification(
                            this.generateAction( android.R.drawable.ic_media_play,"ActionUpdate", ACTION_PLAY),
                            youTubeVideo!! )
                }

            ACTION_STOP ->
                notificationHandler?.apply {
                    this.buildNotification(
                            this.generateAction( android.R.drawable.ic_media_ff,"ActionUpdate", ACTION_STOP),
                            youTubeVideo!! )
                }

        }

    }

    override fun onPrepared(mp: MediaPlayer) {
        isStarting = false
    }

    companion object {

        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_STOP = "action_stop"

        private var youTubeExtractor: YouTubeExtractor? = null
    }

}
