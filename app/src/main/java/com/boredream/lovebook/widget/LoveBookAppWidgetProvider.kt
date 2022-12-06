package com.boredream.lovebook.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.boredream.lovebook.R
import com.boredream.lovebook.receiver.AppWidgetActionBroadcastReceiver
import com.boredream.lovebook.ui.trace.TraceMapActivity


/**
 * https://developer.android.google.cn/guide/topics/appwidgets?hl=en#AppWidgetProvider
 * AppWidgetProvider 类扩展了 BroadcastReceiver 作为一个辅助类来处理应用微件广播。AppWidgetProvider 仅接收与应用微件有关的事件广播，例如当更新、删除、启用和停用应用微件时发出的广播。
 */
class LoveBookAppWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_UPDATE = "com.boredream.lovebook.widget.ACTION_UPDATE"
        const val EXTRA_BTN_TEXT = "com.boredream.lovebook.widget.EXTRA_BTN_TEXT"

        fun updateBtn(context: Context, btnText: String) {
            val intent = Intent(ACTION_UPDATE).apply {
                putExtra(EXTRA_BTN_TEXT, btnText)
            }
            context.sendBroadcast(intent)
        }
    }

    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    override fun onReceive(context: Context, intent: Intent) {
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
        if (intent.action == ACTION_UPDATE) {
            val btnText = intent.getStringExtra(EXTRA_BTN_TEXT)

            val views = createRemoteView(context, btnText)

            // Tell the AppWidgetManager to perform an update on the current app widget
            ComponentName(context, LoveBookAppWidgetProvider::class.java).let {
                // TODO: 尽量使用 partiallyUpdateAppWidget
                appWidgetManager.updateAppWidget(it, views)
            }

            println("LoveBookAppWidgetProvider onReceive update $btnText")
        }
        super.onReceive(context, intent)
    }

    // TODO: 这里默认接收的是桌面widget的更新
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        println("LoveBookAppWidgetProvider onUpdate $appWidgetIds")
        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            val views = createRemoteView(context)
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun createRemoteView(context: Context, btnText: String? = null): RemoteViews {
        val pendingIntent: PendingIntent =
            AppWidgetActionBroadcastReceiver.createToggleTracePendingIntent(context)

        return RemoteViews(
            context.packageName,
            R.layout.appwidget_lovebook,
        ).apply {
            setTextViewText(R.id.btn_start_trace, btnText)
            setOnClickPendingIntent(R.id.btn_start_trace, pendingIntent)
        }
    }

}