package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.LocalTraceRecordDataSource
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val dataSource: LocalTraceRecordDataSource,
) :
    BaseLoadRepository<TraceRecord>(service) {

//    suspend fun getList(groupId: String) = getList { service.getTraceRecordList(groupId) }
//    suspend fun add(data: TraceRecord) = commit { service.addTraceRecord(data) }
//    suspend fun update(data: TraceRecord) = commit { service.updateTraceRecord(data.id!!, data) }
//    suspend fun delete(id: String) = commit { service.deleteTraceRecord(id) }

    suspend fun add(data: TraceRecord): ResponseEntity<Boolean> {
        // TODO: service
        return commit { dataSource.save(data) }
    }

    suspend fun getList(): ResponseEntity<List<TraceRecord>> {
        return getList { dataSource.loadTraceList() }
    }

}