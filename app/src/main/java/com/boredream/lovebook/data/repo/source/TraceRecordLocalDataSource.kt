package com.boredream.lovebook.data.repo.source

import android.util.Log
import androidx.core.os.trace
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.db.AppDatabase
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

    fun addTraceRecord(record: TraceRecord) {
        val res = traceRecordDao.insert(record)
        Log.i(TAG, "addTraceRecord: $res")
    }

    fun addTraceLocation(traceLocation: List<TraceLocation>) {
        val res = traceLocationDao.insertAll(traceLocation)
        Log.i(TAG, "addTraceLocation: $res")
    }

}