package eu.caraus.appsflastfm.common.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.app.NotificationCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import eu.caraus.appsflastfm.R
import eu.caraus.appsflastfm.services.youtube.YoutubePlayerService
import eu.caraus.appsflastfm.services.youtube.model.youtube.YouTubeVideo
import eu.caraus.appsflastfm.ui.main.MainActivity

class NotificationHandler( private val context: Context ) {

    private var builder: NotificationCompat.Builder? = null

    fun buildNotification( action : NotificationCompat.Action, video : YouTubeVideo ) {

        val style = android.support.v4.media.app.NotificationCompat.MediaStyle()

        val serviceIntentStop = getServiceIntent().apply {
            this.action = YoutubePlayerService.ACTION_STOP
        }

        val servicePendingStop = getPendingIntent( serviceIntentStop , 1)

        val activityIntentOpen = getActivityIntent().apply {
            this.action = Intent.ACTION_MAIN
            this.addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val activityPendingOpen = getPendingIntent( activityIntentOpen, 0)

        builder = NotificationCompat.Builder( context, "1")
        builder!!.setSmallIcon(R.mipmap.ic_launcher)
        builder!!.setContentTitle(video.title)
        builder!!.setContentInfo(video.duration)
        builder!!.setShowWhen(false)
        builder!!.setContentIntent(activityPendingOpen)
        builder!!.setDeleteIntent( servicePendingStop )
        builder!!.setOngoing(false)
        builder!!.setSubText(video.viewCount)
        builder!!.setStyle(style)

        //load bitmap for largeScreen
        if (video.thumbnailURL != null && !video.thumbnailURL!!.isEmpty()) {
            //TODO : handle notification
            Picasso.with(context).load(video!!.thumbnailURL).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    updateNotificationLargeIcon(bitmap)
                }

                override fun onBitmapFailed(errorDrawable: Drawable) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                }
            })
        }

        builder!!.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", YoutubePlayerService.ACTION_PREVIOUS))
        builder!!.addAction(action)
        builder!!.addAction(generateAction(android.R.drawable.ic_media_next, "Next", YoutubePlayerService.ACTION_NEXT))

        style.setShowActionsInCompactView(0, 1, 2)


        getSystemService().notify( 1, builder!!.build())

    }


    private fun updateNotificationLargeIcon( bitmap : Bitmap ) {
        builder!!.setLargeIcon(bitmap)
        getSystemService().notify( 1, builder!!.build())
    }

    fun generateAction( icon: Int, title: String, intentAction: String) : NotificationCompat.Action {
        val intent = getServiceIntent().apply {
            action = intentAction
        }
        return NotificationCompat.Action.Builder( icon, title, getPendingIntent( intent,0 ) ).build()
    }

    private fun getServiceIntent() : Intent
            = Intent( context, YoutubePlayerService::class.java)

    private fun getActivityIntent() : Intent
            = Intent( context, MainActivity::class.java)

    private fun getPendingIntent( intent : Intent, requestCode : Int = 0 ) : PendingIntent
            = PendingIntent.getService( context, requestCode, intent, 0)

    private fun getSystemService() : NotificationManager
            = context.getSystemService( Context.NOTIFICATION_SERVICE ) as NotificationManager

}