package nl.avans.marvelapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import nl.avans.marvelapp.MainActivity

object NotificationService {
    private var AUTO_INCREMENT_ID = 0 // keeps track of the last id used

    data class NotificationData (
        var channel: ChannelData,
        var title: String,
        var text: String,
        var smallIcon: Int,
        var largeIcon: Int
    )

    data class ChannelData(
        val id: String,
        val description: String,
        var lights: Boolean = true,
        var lightColor: Int = Color.GRAY,
        var vibration: Boolean = false,
    )

    fun build(context: Context, data: NotificationData) : Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(context, data.channel.id)
            .setContentTitle(data.title)
            .setContentText(data.text)
            .setSmallIcon(data.smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, data.largeIcon))
            .setContentIntent(pendingIntent)
            .build()
    }

    fun send(context: Context, channel: ChannelData, notification: Notification) : Int {
        val id = AUTO_INCREMENT_ID++
        update(id, context, channel, notification)
        return id
    }

    fun update(id: Int, context: Context, channel: ChannelData, notification: Notification) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(channel.id, channel.description, NotificationManager.IMPORTANCE_HIGH)

        notificationChannel.enableLights(channel.lights)
        notificationChannel.lightColor = channel.lightColor
        notificationChannel.enableVibration(channel.vibration)
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(id, notification)
    }
}