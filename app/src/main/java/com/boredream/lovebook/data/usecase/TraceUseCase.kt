package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.TraceRepository
import javax.inject.Inject

/**
 * TODO: 职责划分？
 * 和 repo 的区别，repo 处理数据，use case 处理业务逻辑。
 * 简单的业务，repo = use case，复杂的 use case 一般整合多个 repo。
 * 不需要单例？
 */
class TraceUseCase @Inject constructor(
    val locationRepository: LocationRepository,
    val traceRepository: TraceRepository,
) {

    var isTracing = false

    fun startLocation() {
        locationRepository.onLocationSuccess = ::onLocationSuccess
        locationRepository.startLocation()
    }

    fun stopLocation() {
        locationRepository.stopLocation()
    }

    fun startTrace() {
        isTracing = true
    }

    fun stopTrace() {
        isTracing = false
        traceRepository.traceList
    }

    private fun onLocationSuccess(location: TraceLocation) {
        if (isTracing) {
            traceRepository.appendTracePoint(location)
        }
    }

}