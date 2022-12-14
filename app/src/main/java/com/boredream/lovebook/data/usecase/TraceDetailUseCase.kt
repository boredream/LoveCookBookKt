package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.data.repo.TraceLocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceDetailUseCase @Inject constructor(
    private val traceRecordRepository: TraceRecordRepository,
    private val traceLocationRepository: TraceLocationRepository,
) {

    fun init(traceRecordId: String) {
        traceLocationRepository.init(traceRecordId)
    }

    suspend fun getTraceList() = traceLocationRepository.getList()

}