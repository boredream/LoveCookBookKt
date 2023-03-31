package com.boredream.lovebook.utils

import com.boredream.lovebook.data.event.SyncStatusEvent
import org.greenrobot.eventbus.EventBus

object SyncUtils {

    var isSyncing = false
        set(value) {
            field = value
            EventBus.getDefault().post(SyncStatusEvent(value))
        }

}