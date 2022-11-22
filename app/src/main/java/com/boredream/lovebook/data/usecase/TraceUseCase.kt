package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import javax.inject.Inject

/**
 * TODO: 职责划分？
 * 和 repo 的区别，repo 处理数据，use case 处理业务逻辑。
 * 简单的业务，repo = use case，复杂的 use case 一般整合多个 repo。
 * 不需要单例？
 */
class TraceUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val traceRecordRepository: TraceRecordRepository,
) {

    fun getMyLocation() = locationRepository.myLocation

    fun startLocation() {
        locationRepository.startLocation()
    }

    fun stopLocation() {
        locationRepository.stopLocation()
    }

    fun startTrace() {
        locationRepository.startTrace()
    }

    fun stopTrace() {
        locationRepository.stopTrace()

        // 记录数据
        // traceRecordRepository.saveTraceList(locationRepository.traceList)
    }

    fun setOnLocationSuccess(onLocationSuccess: (location: TraceLocation) -> Unit) {
        locationRepository.onLocationSuccess = onLocationSuccess
    }

    fun setOnTraceSuccess(onTraceSuccess: (tracePointList: ArrayList<TraceLocation>) -> Unit) {
        locationRepository.onTraceSuccess = onTraceSuccess
    }

}