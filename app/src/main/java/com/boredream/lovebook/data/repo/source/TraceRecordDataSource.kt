package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dto.PageResultDto

interface TraceRecordDataSource {

    suspend fun add(data: TraceRecord): ResponseEntity<TraceRecord>

    suspend fun update(data: TraceRecord): ResponseEntity<TraceRecord>

    suspend fun delete(data: TraceRecord): ResponseEntity<TraceRecord>

}