package com.boredream.lovebook.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boredream.lovebook.data.TraceLocation

@Dao
interface TraceLocationDao {

    @Query("SELECT * FROM TraceLocation WHERE traceRecordId = :traceRecordId")
    fun loadByTraceRecordId(traceRecordId: String): List<TraceLocation>

    @Insert
    fun insertAll(dataList: List<TraceLocation>)

    @Delete
    fun delete(data: TraceLocation)

}