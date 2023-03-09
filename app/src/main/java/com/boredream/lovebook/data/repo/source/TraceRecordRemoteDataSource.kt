package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject

class TraceRecordRemoteDataSource @Inject constructor(private val service: ApiService): TraceRecordDataSource {

    override suspend fun add(data: TraceRecord): ResponseEntity<TraceRecord> {
        // 提交数据时，放在 traceListStr里，减少报文大小
        val sb = StringBuilder()
        data.traceList.forEach {
            sb.append("_").append(it.time)
                .append(",").append(it.latitude)
                .append(",").append(it.longitude)
        }
        if(sb.isNotEmpty()) {
            data.traceListStr = sb.substring(1)
        }
        data.traceList.clear()
        return try {
            service.addTraceRecord(data)
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

    override suspend fun getPageList(page: Int): ResponseEntity<PageResultDto<TraceRecord>> {
        return try {
            service.getTraceRecordList(page)
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

    override suspend fun update(data: TraceRecord): ResponseEntity<TraceRecord> {
        return try {
            service.updateTraceRecord(data.id!!, data)
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

    override suspend fun delete(data: TraceRecord): ResponseEntity<TraceRecord> {
        return try {
            service.deleteTraceRecord(data.id!!)
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

}