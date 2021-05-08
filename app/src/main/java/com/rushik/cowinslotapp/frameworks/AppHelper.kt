package com.rushik.cowinslotapp.frameworks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.rushik.cowinslotapp.R

class AppHelper {
    companion object {
        fun withTrailingSlash(str: String?): String {
            if (str == null) {
                return ""
            }
            return if (str.endsWith("/")) str else "$str/"
        }

        fun getNotification(context: Context, title: String = "App is Running"): Notification? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "slotApp",
                    "slotApp",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager?.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(context, "slotApp")
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            return builder.build()
        }

        fun showNotification(
            context: Context,
            notificationId: Int = 123,
            title: String = "Title",
            body: String = "Body",
            channel_id: String = "slotApp",
            channel_name: String = "slotApp",
        ) {
            val notificationManager = NotificationManagerCompat.from(context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channel_id,
                    channel_name,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, "slotApp")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setDefaults(Notification.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(notificationId, builder.build())
        }

    }
}