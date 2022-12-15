package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.base.BaseUseCase
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.TraceLocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceDetailUseCase @Inject constructor(
    private val traceRecordRepository: TraceRecordRepository,
    private val traceLocationRepository: TraceLocationRepository,
) : BaseUseCase() {

    fun init(traceRecordId: String) {
        traceLocationRepository.init(traceRecordId)
    }

    suspend fun getTraceList() = traceLocationRepository.getList()
    suspend fun updateRecord(data: TraceRecord) = traceRecordRepository.update(data)

}