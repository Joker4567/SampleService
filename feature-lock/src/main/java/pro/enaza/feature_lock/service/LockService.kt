package pro.enaza.feature_lock.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import pro.enaza.feature_lock.R

class LockService : Service() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(NOTIFICATION_CHANNEL, createNotification())
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification? {
        return NotificationCompat.Builder(this, NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Синхронизация данных")
                .setContentText("Проверка запущенных активностей")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .build()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_ID, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Синхронизация данных")
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(false) // автоматически закрыть уведомление после нажатия
                .setOngoing(true)
                .build()
        startForeground(NOTIFICATION_CHANNEL, notification)
    }

    companion object {
        const val NOTIFICATION_CHANNEL = 12
        const val NOTIFICATION_ID = "channel_mobi_games"
        const val NOTIFICATION_CHANNEL_ID = "pro.enaza.sampleservice"
    }
}