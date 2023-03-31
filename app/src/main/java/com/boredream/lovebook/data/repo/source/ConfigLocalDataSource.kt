package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.utils.DataStoreUtils
import javax.inject.Inject

class ConfigLocalDataSource @Inject constructor() {

    companion object {
        const val DATA_SYNC_TIMESTAMP_KEY = "data_sync_timestamp_key"
    }

    fun <T> set(key: String, value: T) {
        DataStoreUtils.putSyncData(key, value)
    }

    fun getLong(key: String) = DataStoreUtils.readLongData(key, 0L)

}