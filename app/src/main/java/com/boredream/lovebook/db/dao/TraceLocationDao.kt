package com.boredream.lovebook.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.boredream.lovebook.data.TraceLocation

@Dao
interface TraceLocationDao {

    @Query("SELECT * FROM TraceLocation WHERE traceRecordDbId = :traceRecordDbId")
    suspend fun loadByTraceRecordId(traceRecordDbId: Long): List<TraceLocation>

    @Insert
    suspend fun insertAll(dataList: List<TraceLocation>)

    @Delete
    suspend fun delete(data: TraceLocation)

    @Query("DELETE FROM TraceLocation WHERE traceRecordDbId = :traceRecordDbId")
    suspend fun deleteByTraceRecordId(traceRecordDbId: Long)

}