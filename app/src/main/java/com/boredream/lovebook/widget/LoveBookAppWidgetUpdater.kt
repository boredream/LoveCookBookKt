package com.boredream.lovebook.widget

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.service.TraceLocationService
import com.boredream.lovebook.utils.TraceUtils

object LoveBookAppWidgetUpdater {

    private val appWidgetManager: AppWidgetManager
    private val appWidgetIds: IntArray

    init {
        val context = Utils.getApp()
        appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, LoveBookAppWidgetProvider::class.java)
        appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
    }

    private fun getRemoteViews() = RemoteViews(AppUtils.getAppPackageName(), R.layout.appwidget_lovebook)

    /**
     * 更新追踪状态
     */
    fun updateTraceStatus(context: Context, isTracing: Boolean) {
        var traceInfo: String? = null
        if (!isTracing) {
            traceInfo = "轨迹信息"
        }
        update(context, true, isTracing, traceInfo)
    }

    /**
     * 更新轨迹信息
     */
    fun updateTraceInfo(context: Context, traceList: ArrayList<TraceLocation>) {
        val traceInfo = "轨迹信息：${TraceUtils.calculateDuration(traceList)} 秒，" +
                "${TraceUtils.calculateDistance(traceList)} 米"
        update(context, true, null, traceInfo)
    }

    /**
     * 更新到默认状态
     */
    fun updateDefault(context: Context) {
        update(context, isPartiallyUpdate = false, isTracing = false, traceInfo = "轨迹信息")
    }

    /**
     * 更新
     */
    fun update(context: Context, isPartiallyUpdate: Boolean = false, isTracing: Boolean?, traceInfo: String?) {
        val views = getRemoteViews()

        // 更新记录状态
        isTracing?.let {
            val toggleTraceAction = !it
            val btnToggleTraceText = if (toggleTraceAction) "开始记录" else "停止记录"

            val intent = Intent(context, TraceLocationService::class.java)
            intent.putExtra(BundleKey.TOGGLE_TRACE, toggleTraceAction)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getService(context, 0, intent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
            } else {
                PendingIntent.getService(context, 0, intent, FLAG_UPDATE_CURRENT)
            }

            views.setTextViewText(R.id.btn_toggle_trace, btnToggleTraceText)
            views.setOnClickPendingIntent(R.id.btn_toggle_trace, pendingIntent)
            LogUtils.i("updateTraceStatus $toggleTraceAction")
        }

        // 更新轨迹信息 距离+时间信息
        traceInfo?.let {
            views.setTextViewText(R.id.tv_trace_info, it)
            // LogUtils.i("updateTraceInfo $it")
        }

        if(isPartiallyUpdate) {
            appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, views)
        } else {
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

}