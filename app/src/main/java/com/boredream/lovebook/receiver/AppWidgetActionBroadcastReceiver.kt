package com.boredream.lovebook.receiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppWidgetActionBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TOGGLE_TRACE = "com.boredream.lovebook.receiver.ACTION_TOGGLE_TRACE"

        fun register(context: Context) : AppWidgetActionBroadcastReceiver {
            val receiver = AppWidgetActionBroadcastReceiver()
            val filter = IntentFilter(ACTION_TOGGLE_TRACE)
            context.registerReceiver(receiver, filter)
            return receiver
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun createToggleTracePendingIntent(context: Context): PendingIntent {
            val intent = Intent(ACTION_TOGGLE_TRACE)
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_TOGGLE_TRACE -> toggleTrace(intent)
        }
    }

    private fun toggleTrace(intent: Intent) {
        println("AppWidgetActionBroadcastReceiver toggleTrace")
    }
}