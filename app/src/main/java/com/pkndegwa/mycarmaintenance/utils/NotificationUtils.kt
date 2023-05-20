package com.pkndegwa.mycarmaintenance.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.ui.MainActivity

private const val NOTIFICATION_ID = 0

// Extension function to send messages.
/**
 * Builds and delivers the notification.
 *
 * @param applicationContext, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the pending intent for the notification, which launches an activity or fragment
    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.remindersFragment)
        .createPendingIntent()

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.reminder_notification_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}