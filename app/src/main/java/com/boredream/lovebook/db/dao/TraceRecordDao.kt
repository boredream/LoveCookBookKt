package com.boredream.lovebook.db.dao

import androidx.room.*
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.db.relation.TraceRecordWithLocation

@Dao
interface TraceRecordDao {

    @Query("SELECT * FROM TraceRecord")
    suspend fun getAll(): List<TraceRecord>

    @Transaction
    @Query("SELECT * FROM TraceRecord WHERE id = :id")
    suspend fun loadById(id: String): TraceRecord

    @Insert
    suspend fun insert(traceRecord: TraceRecord): Long

    @Delete
    suspend fun delete(data: TraceRecord)

}