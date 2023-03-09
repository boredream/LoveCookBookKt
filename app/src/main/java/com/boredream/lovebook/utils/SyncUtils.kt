package com.boredream.lovebook.utils

import com.blankj.utilcode.util.LogUtils

object SyncUtils {

    private const val DATA_SYNC_TIMESTAMP_KEY = "data_sync_timestamp_key"

    fun get() = DataStoreUtils.readLongData(DATA_SYNC_TIMESTAMP_KEY, 0L)

    /**
     * 更新同步全局时间戳，一般在本地数据更新成功后调用
     */
    fun update(syncTimestamp: Long?) {
        val timestamp = syncTimestamp ?: return

        val localTimestamp = get()
        if (timestamp > localTimestamp) {
            // 如果数据同步时间比本地保存的新，替换之
            DataStoreUtils.putSyncData(DATA_SYNC_TIMESTAMP_KEY, timestamp)
            LogUtils.i("update syncTimestamp $timestamp")
        }
    }

}