package com.boredream.lovebook.data.repo.source

import androidx.room.Transaction
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.db.AppDatabase
import javax.inject.Inject

class TraceRecordLocalDataSource @Inject constructor(appDatabase: AppDatabase) :
    TraceRecordDataSource {

    private val traceRecordDao = appDatabase.traceRecordDao()
    private val traceLocationDao = appDatabase.traceLocationDao()

    suspend fun getUnSyncedTraceRecord(): ResponseEntity<ArrayList<TraceRecord>> {
        return try {
            val list = traceRecordDao.loadUnSynced()
            ResponseEntity.success(ArrayList(list))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    @Transaction
    override suspend fun add(data: TraceRecord): ResponseEntity<TraceRecord> {
        var dbId: Long = -1
        try {
            dbId = traceRecordDao.insertOrUpdate(data)
        } catch (e: Exception) {
            //
        }

        if (dbId <= 0) {
            return ResponseEntity(null, 500, "数据插入失败")
        }

        data.dbId = dbId
        data.traceList.forEach { it.traceRecordDbId = dbId }
        return try {
            traceLocationDao.insertAll(data.traceList)
            ResponseEntity.success(data)
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    suspend fun getTraceRecordById(id: String): ResponseEntity<TraceRecord> {
        return try {
            ResponseEntity.success(traceRecordDao.loadById(id))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    suspend fun getTraceRecordByDbId(dbId: Long): ResponseEntity<TraceRecord> {
        return try {
            ResponseEntity.success(traceRecordDao.loadByDbId(dbId))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    suspend fun getList(): ResponseEntity<ArrayList<TraceRecord>> {
        return try {
            ResponseEntity.success(ArrayList(traceRecordDao.loadAll()))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    override suspend fun getPageList(page: Int): ResponseEntity<PageResultDto<TraceRecord>> {
        // page 从1开始
        val limit = 20
        val offset = (page - 1) * limit
        return try {
            ResponseEntity.success(PageResultDto(page, traceRecordDao.loadByPage(limit, offset)))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    suspend fun getTraceLocationList(traceRecordDbId: Long): ResponseEntity<ArrayList<TraceLocation>> {
        return try {
            ResponseEntity.success(ArrayList(traceLocationDao.loadByTraceRecordId(traceRecordDbId)))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    override suspend fun update(data: TraceRecord): ResponseEntity<Boolean> {
        return try {
            return ResponseEntity.success(traceRecordDao.insertOrUpdate(data) > 0)
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    override suspend fun delete(data: TraceRecord): ResponseEntity<Boolean> {
        return try {
            return ResponseEntity.success(traceRecordDao.delete(data) > 0)
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

}