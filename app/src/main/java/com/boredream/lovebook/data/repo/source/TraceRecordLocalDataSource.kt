package com.boredream.lovebook.data.repo.source

import android.util.Log
import androidx.room.Transaction
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dto.PageResultDto
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

    @Transaction
    suspend fun addTraceRecord(record: TraceRecord): ResponseEntity<Boolean> {
        val dbId: Long
        try {
            dbId = traceRecordDao.insert(record)
        } catch (e: Exception) {
            return ResponseEntity(false, 500, e.toString())
        }

        if(dbId <= 0) {
            return ResponseEntity(false, 500, "数据插入失败")
        }

        record.dbId = dbId
        record.traceList.forEach { it.traceRecordDbId = dbId }
        traceLocationDao.insertAll(record.traceList)
        Log.i(TAG, "addTraceRecord: $dbId")
        return ResponseEntity.success(true)
    }

    suspend fun getTraceRecordList(page: Int): ResponseEntity<PageResultDto<TraceRecord>> {
        // page 从1开始
        val limit = 20
        val offset = (page - 1) * limit
        val pages = 1f * traceRecordDao.getRowCount() / limit + 0.5f
        val list = traceRecordDao.loadByPage(limit, offset)
        return ResponseEntity.success(PageResultDto(page, pages.toInt(), list))
    }

    suspend fun getTraceLocationList(traceRecordDbId: Long): ResponseEntity<ArrayList<TraceLocation>> {
        val list = traceLocationDao.loadByTraceRecordId(traceRecordDbId)
        return ResponseEntity.success(ArrayList(list))
    }

    suspend fun deleteTraceRecord(traceRecord: TraceRecord): ResponseEntity<Boolean> {
        val delete = traceRecordDao.delete(traceRecord)
        return ResponseEntity.success(delete > 0)
    }

}