package com.boredream.lovebook.utils

object SyncUtils {

    private const val DATA_SYNC_TIMESTAMP_KEY = "data_sync_timestamp_key"

    fun get() = DataStoreUtils.readLongData(DATA_SYNC_TIMESTAMP_KEY, 0L)

    fun update(syncTimestamp: Long?) {
        val timestamp = syncTimestamp ?: return

        val localTimestamp = get()
        if (timestamp > localTimestamp) {
            // 如果数据同步时间比本地保存的新，替换之
            DataStoreUtils.putSyncData(DATA_SYNC_TIMESTAMP_KEY, timestamp)
        }
    }


}