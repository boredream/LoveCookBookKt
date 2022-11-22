package com.boredream.lovebook.ui.trace

import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.CollectionUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TraceMapViewModel @Inject constructor(
    val repository: LocationRepository
) : BaseViewModel() {

    // TODO: 如何更好的设计地图这种 命令式传统view 和 vm 的关系？

    var firstLocation = true

    private val _mapEvent = SingleLiveEvent<MapUiEvent>()
    val mapEvent: LiveData<MapUiEvent> = _mapEvent

    fun startLocation() {
        repository.onLocationSuccess = ::onLocationSuccess
        repository.onTraceSuccess = ::onTraceSuccess

        // TODO: 先绘制我已有的location

        repository.startLocation()
    }

    fun stopLocation() {
        repository.stopLocation()
    }

    fun toggleTrace() {
        TODO("Not yet implemented")
    }

    fun startTrace() {
        TODO("Not yet implemented")
    }

    fun locationMe() {
        repository.myLocation?.let { _mapEvent.value = MoveToLocation(it) }
    }

    private fun onLocationSuccess(location: TraceLocation) {
        if (firstLocation) {
            locationMe()
        }
        firstLocation = false

        _mapEvent.value = DrawMyLocation(location)
    }

    private fun onTraceSuccess(list: ArrayList<TraceLocation>) {
        if (CollectionUtils.isEmpty(list) || list.size < 2) {
            return
        }
        val stepList = arrayListOf(list[list.lastIndex - 1], list[list.lastIndex])
        _mapEvent.value = DrawTraceLine(stepList)
    }

}