package com.boredream.lovebook.service

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.boredream.lovebook.R
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.receiver.AppWidgetActionBroadcastReceiver
import com.boredream.lovebook.ui.trace.TraceMapActivity
import com.boredream.lovebook.widget.LoveBookAppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * 追踪前台服务，保证切换到后台时，依然可以继续定位
 * https://developer.android.com/guide/components/foreground-services
 */
@AndroidEntryPoint
class TraceLocationService : Service() {

    companion object {
        const val SERVICE_ID = 110119120
        const val CHANNEL_ID = "com.boredream.lovebook.service.tracelocation"
    }

    @Inject
    lateinit var traceUseCase: TraceUseCase

    private lateinit var widgetActionReceiver: AppWidgetActionBroadcastReceiver

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, TraceMapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, getString(R.string.app_name), IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24) // 设置通知的图标
                .setContentTitle(getString(R.string.app_name)) // 设置标题的标题
                .setContentText("记录轨迹ing...") // 设置的标题内容
                .setContentIntent(pendingIntent)
                .build()
        } else {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("记录轨迹ing...")
                .setContentIntent(pendingIntent)
                .build()
        }

        // 只做保活用，无需任何处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(SERVICE_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(SERVICE_ID, notification)
        }

        widgetActionReceiver = AppWidgetActionBroadcastReceiver.register(this)

        traceUseCase.addStatusChangeListener(onStatusChange)
        traceUseCase.startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(widgetActionReceiver)

        traceUseCase.removeStatusChangeListener(onStatusChange)
        traceUseCase.stopLocation()
    }

    private var onStatusChange: (status: Int) -> Unit = {
        // 定位状态变化
        println("TraceLocationService onStatusChange $it")
        if(it == TraceUseCase.STATUS_TRACE) {
            LoveBookAppWidgetProvider.updateBtn(this, "停止记录轨迹")
        } else {
            LoveBookAppWidgetProvider.updateBtn(this, "开始记录轨迹")
        }
    }

}