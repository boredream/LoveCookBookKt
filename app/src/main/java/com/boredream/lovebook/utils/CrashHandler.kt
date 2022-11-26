package com.boredream.lovebook.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.boredream.lovebook.ui.splash.SplashActivity
import com.jakewharton.processphoenix.ProcessPhoenix

class CrashHandler(val context : Context) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        val defaultIntent = Intent(Intent.ACTION_MAIN, null)
        defaultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        defaultIntent.addCategory(Intent.CATEGORY_DEFAULT)
        defaultIntent.component = ComponentName(context, SplashActivity::class.java)
        ProcessPhoenix.triggerRebirth(context, defaultIntent)
    }
}