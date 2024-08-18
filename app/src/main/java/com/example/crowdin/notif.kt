package com.example.crowdin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

var CHANNEL_ID = "alerts"
var CHANNEL_NAME = "Alert Notifications"
var CHANNEL_DESCRIPTION = "High priority alerts, from NearBy"

data class Notification(val title: String, val message: String)

val notification = mutableStateOf<Notification?>(null)
val isTherePendingAlert = mutableStateOf(false)

@Composable
fun Notify() {
    val ctx = LocalContext.current
    val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = CHANNEL_DESCRIPTION
    }
    val nManager: NotificationManager =
        getSystemService(ctx, NotificationManager::class.java) as NotificationManager
    nManager.createNotificationChannel(channel)
    val resources = ctx.resources
    val imgBitmap = BitmapFactory.decodeResource(resources, R.drawable.alert)

    val intent2 = Intent()
    intent2.action = Intent.ACTION_DELETE

    val pendingIntent1 = PendingIntent.getActivity(ctx, 5, intent2, PendingIntent.FLAG_IMMUTABLE)
    val pendingIntent2 = PendingIntent.getActivity(ctx, 6, Intent(ctx, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)

    when {
        isTherePendingAlert.value -> {
            isTherePendingAlert.value = false
            val nBuilder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setContentTitle(
                    notification.value?.title ?: "Alert"
                )
                .setContentText(
                    notification.value?.message ?: "There is an alert (?)"
                )
                .setSmallIcon(R.drawable.tiger)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(imgBitmap)
                .setContentIntent(pendingIntent1)
                .setAutoCancel(true)
                .addAction(0, "SEE DETAILS", pendingIntent2)
                .addAction(0, "DISMISS", pendingIntent1)
                .build()
            nManager.notify(1, nBuilder)
        }
    }
}
