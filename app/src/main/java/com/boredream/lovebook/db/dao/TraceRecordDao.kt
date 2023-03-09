package com.boredream.lovebook.db.dao

import androidx.room.*
import com.boredream.lovebook.data.TraceRecord

@Dao
interface TraceRecordDao {

    @Query("SELECT * FROM TraceRecord WHERE synced = 0")
    suspend fun loadUnSynced(): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord WHERE isDelete = 0 limit :limit offset :offset")
    suspend fun loadByPage(limit: Int, offset: Int): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord WHERE isDelete = 0")
    suspend fun loadAll(): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord WHERE dbId = :dbId")
    suspend fun loadByDbId(dbId: String): TraceRecord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(traceRecord: TraceRecord): Long

    @Delete
    suspend fun delete(data: TraceRecord): Int

    @Update
    suspend fun update(data: TraceRecord): Int

}