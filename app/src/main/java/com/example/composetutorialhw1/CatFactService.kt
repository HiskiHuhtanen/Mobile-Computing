package com.example.composetutorialhw1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.room.Room


//AAAAAA
//https://www.youtube.com/watch?v=YZL-_XJSClc
//MAKING BACKGROUND TASKS IS ANNOYING!!!

class CatFactService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var db: AppDatabase
    private lateinit var messageDao: MessageDao

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
            .enableMultiInstanceInvalidation()
            .build()
        //same comment as mainActivity on multiInstance
        messageDao = db.messageDao()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Cool App")
            .setContentText("Fetching info!")
            .build()
        startForeground(1, notification)

        startCatBot(applicationContext, messageDao)
    }

    enum class Actions {
        START, STOP
    }

}