package com.example.composetutorialhw1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

//https://stackoverflow.com/questions/74638579/how-to-control-whether-to-display-notification-or-not-based-on-the-screen-that-i
fun pushNotification(context: Context, author: String, text: String) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { //stops the errors so we will go with this
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val notification = NotificationCompat.Builder(context, "channel_id")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(author)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    NotificationManagerCompat.from(context).notify(1, notification)
}