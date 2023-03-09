package com.boredream.lovebook.data.repo.source

import androidx.room.Transaction
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
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
        var insert: Long = -1
        try {
            insert = traceRecordDao.insertOrUpdate(data)
        } catch (e: Exception) {
            //
        }

        if (insert <= 0) {
            return ResponseEntity(null, 500, "数据插入失败")
        }

        data.traceList.forEach { it.traceRecordId = data.dbId }
        return try {
            traceLocationDao.deleteByTraceRecordId(data.dbId)
            traceLocationDao.insertAll(data.traceList)
            ResponseEntity.success(data)
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    suspend fun getTraceRecordByDbId(dbId: String): ResponseEntity<TraceRecord> {
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

    suspend fun getTraceLocationList(traceRecordDbId: String): ResponseEntity<ArrayList<TraceLocation>> {
        return try {
            ResponseEntity.success(ArrayList(traceLocationDao.loadByTraceRecordId(traceRecordDbId)))
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

    override suspend fun update(data: TraceRecord): ResponseEntity<TraceRecord> {
        var update: Int = -1
        try {
            update = traceRecordDao.update(data)
        } catch (e: Exception) {
            //
        }
        if (update <= 0) {
            return ResponseEntity(null, 500, "数据更新失败")
        }
        return ResponseEntity.success(data)
    }

    @Transaction
    override suspend fun delete(data: TraceRecord): ResponseEntity<TraceRecord> {
        var delete: Int = -1
        try {
            // 软删除，方便同步用
            data.isDelete = true
            delete = traceRecordDao.update(data)
        } catch (e: Exception) {
            //
        }

        if (delete <= 0) {
            return ResponseEntity(null, 500, "数据删除失败")
        }

        return try {
            // location跟着trace record走，不用于判断同步，所以直接删除
            traceLocationDao.deleteByTraceRecordId(data.dbId)
            ResponseEntity.success(data)
        } catch (e: Exception) {
            ResponseEntity(null, 500, e.toString())
        }
    }

}