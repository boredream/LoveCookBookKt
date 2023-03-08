package com.boredream.lovebook.db.dao

import androidx.room.*
import com.boredream.lovebook.data.TraceRecord

@Dao
interface TraceRecordDao {

    @Query("SELECT COUNT(dbId) FROM TraceRecord")
    suspend fun getRowCount(): Int

    @Query("SELECT * FROM TraceRecord WHERE synced = 0")
    suspend fun loadUnSynced(): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord limit :limit offset :offset")
    suspend fun loadByPage(limit: Int, offset: Int): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord")
    suspend fun getAll(): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord WHERE dbId = :dbId")
    suspend fun loadByDbId(dbId: Long): TraceRecord

    @Query("SELECT * FROM TraceRecord WHERE id = :id")
    suspend fun loadById(id: String): TraceRecord

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(traceRecord: TraceRecord): Long

    @Delete
    suspend fun delete(data: TraceRecord): Int

}