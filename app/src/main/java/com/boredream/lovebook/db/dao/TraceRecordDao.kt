package com.boredream.lovebook.data.dao

import androidx.room.*
import com.boredream.lovebook.data.TraceRecord

@Dao
interface TraceRecordDao {

    @Query("SELECT * FROM TraceRecord")
    fun getAll(): List<TraceRecord>

    @Transaction
    @Query("SELECT * FROM TraceRecord WHERE id = :id")
    fun loadById(id: String): TraceRecord

    @Insert
    fun insert(data: TraceRecord)

    @Delete
    fun delete(data: TraceRecord)

}