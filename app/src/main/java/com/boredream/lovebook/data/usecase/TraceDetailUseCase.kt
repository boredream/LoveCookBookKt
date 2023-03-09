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

    private var traceRecordId: String? = null

    fun init(traceRecordId: String) {
        this.traceRecordId = traceRecordId
    }

    suspend fun getTraceList() = traceLocationRepository.getLocalTraceLocationList(traceRecordId!!)
    suspend fun updateRecord(data: TraceRecord) = traceRecordRepository.update(data)

}