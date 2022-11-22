package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.TraceRecordDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceRecordRepository @Inject constructor(
    private val dataSource: TraceRecordDataSource
) {

    fun saveTraceList(traceList: ArrayList<TraceLocation>) {
        dataSource.saveTraceList(traceList)
    }


}