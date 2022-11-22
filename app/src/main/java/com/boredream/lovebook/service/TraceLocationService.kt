package com.boredream.lovebook.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.boredream.lovebook.R

/**
 * 追踪前台服务，保证切换到后台时，依然可以继续定位
 * https://developer.android.com/guide/components/foreground-services
 */
class TraceLocationService : Service() {

    companion object {
        const val SERVICE_ID = 110119120
        const val CHANNEL_ID = "com.boredream.lovebook.service.tracelocation"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, getString(R.string.app_name), IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24) // 设置通知的图标
                .setContentTitle(getString(R.string.app_name)) // 设置标题的标题
                .setContentText("记录轨迹ing...") // 设置的标题内容
                .build()
        } else {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24) // 设置通知的图标
                .setContentTitle(getString(R.string.app_name)) // 设置标题的标题
                .setContentText("记录轨迹ing...") // 设置的标题内容
                .build()
        }

        // 只做保活用，无需任何处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(SERVICE_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(SERVICE_ID, notification)
        }
    }

}