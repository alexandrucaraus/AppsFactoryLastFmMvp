package eu.caraus.appsflastfm.ui.common.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.services.youtube.YoutubePlayerService
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.main.MainActivity
import android.app.NotificationChannel
import android.os.Build
import android.widget.RemoteViews


class NotificationsUtil(private val context: Context ) {

    companion object {

        const val CHANNEL_ID = "eu.caraus.appsflastfm.YOUTUBE_PLAY_SERVICE"

        const val NOTIFICATION_ID = 69

    }

    private var builder : NotificationCompat.Builder? = null

    private fun getServiceIntent() : Intent
            = Intent( context, YoutubePlayerService::class.java)

    private fun getActivityIntent() : Intent
            = Intent( context, MainActivity::class.java)

    private fun getPendingIntent( intent : Intent, requestCode : Int = 0 ) : PendingIntent
            = PendingIntent.getService( context, requestCode, intent, 0)

    private fun getNotificationManager() : NotificationManager
            = context.getSystemService( Context.NOTIFICATION_SERVICE ) as NotificationManager

    private fun getNotificationManagerCompat() : NotificationManagerCompat
            = NotificationManagerCompat.from(context)


    fun buildNotification( pendingAction : String, video : YouTubeVideo ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel( CHANNEL_ID , CHANNEL_ID, importance )
            getNotificationManager().createNotificationChannel(channel)
        }

        val servicePendingStop
                = getPendingIntent( getServiceIntent().apply { this.action = pendingAction } , 0)

        val remoteViews
                = RemoteViews( context.packageName, R.layout.player_notification ).apply{

            setTextViewText( R.id.tvAlbumSong   , video.title )
            setTextViewText( R.id.tvAlbumArtist , "-" )
            setTextViewText( R.id.tvElapsed     , formatTrackLength( video.trackElapsed ))
            setImageViewResource( R.id.ivAlbumImage, R.drawable.icon)

            setOnClickPendingIntent( R.id.pause , servicePendingStop )

        }

        builder = NotificationCompat.Builder( context, CHANNEL_ID ).apply {

            setSmallIcon(R.drawable.ic_play)
            setStyle( NotificationCompat.DecoratedCustomViewStyle() )
            setCustomContentView(remoteViews)

            setDeleteIntent( servicePendingStop )
            setAutoCancel(false)

        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            builder?.setChannelId( CHANNEL_ID )
        }

        builder?.let {
            getNotificationManagerCompat().notify( NOTIFICATION_ID , it.build())
        }

    }

    fun cancel(){
        getNotificationManagerCompat().cancel( NOTIFICATION_ID )
    }

    private fun formatTrackLength( length : Int ) : String {

        val minutes = length / 60
        val seconds = length % 60

        return String.format("%02d:%02d",minutes,seconds)
    }

}