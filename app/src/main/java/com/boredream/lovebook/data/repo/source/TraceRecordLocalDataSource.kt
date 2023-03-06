package com.boredream.lovebook.data.repo.source

import android.util.Log
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.db.AppDatabase
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.TraceUtils
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject

/**
 * 轨迹记录本地数据源
 */
class TraceRecordLocalDataSource @Inject constructor(appDatabase: AppDatabase) {

    private val traceRecordDao = appDatabase.traceRecordDao()
    private val traceLocationDao = appDatabase.traceLocationDao()

    companion object {
        const val TAG = "TraceDataSource"
    }

    /**
     * 新建一条新的轨迹记录
     */
    fun createTraceRecord(): TraceRecord {
        val record = TraceRecord(
            arrayListOf(),
            "缓存线路 " + TimeUtils.getNowString(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            0,
        )
        val res = traceRecordDao.insert(record)
        Log.i(TAG, "createTraceRecord: $res")
        return record
    }

    fun addTraceLocation(traceLocation: TraceLocation) {
        val res = traceLocationDao.insertAll(traceLocation)
        Log.i(TAG, "addTraceLocation: $res")
    }

}