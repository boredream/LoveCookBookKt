package com.boredream.lovebook.data.repo.source

import android.util.Log
import androidx.core.os.trace
import androidx.room.Relation
import androidx.room.Transaction
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.db.AppDatabase
import com.boredream.lovebook.db.relation.TraceRecordWithLocation
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

    @Transaction
    suspend fun addTraceRecord(record: TraceRecord) {
        val dbId = traceRecordDao.insert(record)
        record.traceList.forEach { it.traceRecordDbId = dbId }
        traceLocationDao.insertAll(record.traceList)
        Log.i(TAG, "addTraceRecord: $dbId")
    }

}