package com.boredream.lovebook.service

import android.app.*
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.LogUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.ui.trace.TraceMapActivity
import com.boredream.lovebook.widget.LoveBookAppWidgetUpdater
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

//    private val job: Job = SupervisorJob()
//    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, TraceMapActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(application, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(application, 0, intent, FLAG_UPDATE_CURRENT)
        }

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

        traceUseCase.addStatusChangeListener(onStatusChange)
        traceUseCase.addTraceSuccessListener(onTraceSuccess)

        traceUseCase.startLocation()
        LogUtils.i("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.hasExtra(BundleKey.TOGGLE_TRACE)) {
                val toggleTraceAction = it.getBooleanExtra(BundleKey.TOGGLE_TRACE, false)
                if (toggleTraceAction) {
                    traceUseCase.startLocation()
                    traceUseCase.startTrace()
                } else {
                    // 打开页面/刷新widget进行询问保存？或者直接进行保存
                    traceUseCase.stopTrace()
//                scope.launch {
//                    traceUseCase.saveTraceRecord()
//                }
                    traceUseCase.stopLocation()
                }
                LogUtils.i("TOGGLE_TRACE $toggleTraceAction")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

//        job.cancel()

        traceUseCase.removeStatusChangeListener(onStatusChange)
        traceUseCase.stopLocation()

        LoveBookAppWidgetUpdater.updateTraceStatus(this, true)
    }

    private var onStatusChange: (status: Int) -> Unit = {
        // 定位状态变化
        LogUtils.i("status = $it")
        if (it == TraceUseCase.STATUS_TRACE) {
            LoveBookAppWidgetUpdater.updateTraceStatus(this, true)
        } else {
            LoveBookAppWidgetUpdater.updateTraceStatus(this, false)
        }
    }

    private var onTraceSuccess: (allTracePointList: ArrayList<TraceLocation>) -> Unit = {
        // 定位状态变化
        // println("TraceLocationService allTracePointList $it")
        if (it.size != 0) {
            val timeDiff = System.currentTimeMillis() - it[0].time
            val updateWidgetInterval = 30_000 // widget刷新间隔，单位毫秒
            val timeIndex: Int = (timeDiff % updateWidgetInterval + 500).toInt() / 1000
            if (timeIndex == 0) {
                LogUtils.i("updateTraceInfo $timeDiff $timeIndex")
                LoveBookAppWidgetUpdater.updateTraceInfo(this, it)
            }
        }
    }

}