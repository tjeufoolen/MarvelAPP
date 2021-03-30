package nl.avans.marvelapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import nl.avans.marvelapp.R

object NotificationService {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelID = R.string.app_name
    private val desc = "notifications"

    init {
        notificationManager
    }
}