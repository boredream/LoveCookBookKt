package com.boredream.lovebook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dao.TraceLocationDao
import com.boredream.lovebook.data.dao.TraceRecordDao

@Database(entities = [TraceRecord::class, TraceLocation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun traceRecordDao(): TraceRecordDao

    abstract fun traceLocationDao(): TraceLocationDao

}