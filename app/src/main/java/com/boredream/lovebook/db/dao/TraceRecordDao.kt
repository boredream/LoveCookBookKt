package com.boredream.lovebook.db.dao

import androidx.room.*
import com.boredream.lovebook.data.TraceRecord

@Dao
interface TraceRecordDao {

    @Query("SELECT COUNT(dbId) FROM TraceRecord")
    suspend fun getRowCount(): Int

    @Query("SELECT * FROM TraceRecord limit :limit offset :offset")
    suspend fun loadByPage(limit: Int, offset: Int): List<TraceRecord>

    @Query("SELECT * FROM TraceRecord")
    suspend fun getAll(): List<TraceRecord>

    @Transaction
    @Query("SELECT * FROM TraceRecord WHERE dbId = :dbId")
    suspend fun loadById(dbId: String): TraceRecord

    @Insert
    suspend fun insert(traceRecord: TraceRecord): Long

    @Delete
    suspend fun delete(data: TraceRecord): Int

}