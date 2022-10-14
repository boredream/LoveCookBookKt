package com.boredream.lovebook

import android.app.Application
import com.boredream.lovebook.utils.DataStoreUtils

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        DataStoreUtils.init(this)

    }

}