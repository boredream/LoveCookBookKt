package com.boredream.lovebook.ui.trace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amap.api.mapcore.util.it
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UIEvent
object ShowSaveConfirmDialog : UIEvent()

data class UiState(
    val myLocation: TraceLocation? = null,
)

@HiltViewModel
class TraceMapViewModel @Inject constructor(
    private val traceUseCase: TraceUseCase
) : BaseRequestViewModel<TraceRecord>() {

    // 自定义 DataBinding 连接地图这种 命令式传统view 和 vm 的关系

    // 主UI事件
    private val _uiEvent = SingleLiveEvent<UIEvent>()
    val uiEvent: LiveData<UIEvent> = _uiEvent

    // 单独更新的需要各自LiveData

    // 主UI元素
    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _tracePointListUiState = MutableLiveData(ArrayList<TraceLocation>())
    val tracePointListUiState: LiveData<ArrayList<TraceLocation>> = _tracePointListUiState

    // 是否显示历史轨迹
    private val _isShowHistoryTrace = MutableLiveData(false)
    val isShowHistoryTrace: LiveData<Boolean> = _isShowHistoryTrace

    private val _historyTracePointListUiState =
        MutableLiveData(ArrayList<ArrayList<TraceLocation>>())
    val historyTracePointListUiState: LiveData<ArrayList<ArrayList<TraceLocation>>> =
        _historyTracePointListUiState

    // 是否为跟踪模式
    private val _isFollowing = MutableLiveData(true)
    val isFollowing: LiveData<Boolean> = _isFollowing

    // 是否正在记录轨迹中
    private val _isTracing = MutableLiveData(false)
    val isTracing: LiveData<Boolean> = _isTracing

    /**
     * 切换显示历史轨迹
     */
    fun toggleShowHistoryTrace() {
        val old = _isShowHistoryTrace.value!!
        _isShowHistoryTrace.value = !old

        if (!old) {
            // TODO: 可能是远程获取，需要loading
            viewModelScope.launch {
                val recordList = traceUseCase.getAllHistoryTraceListRecord()
                val historyList = ArrayList<ArrayList<TraceLocation>>()
                recordList.data?.dataList?.let { it -> it.forEach { historyList.add(it.traceList) } }
                _historyTracePointListUiState.value = historyList
            }
        }
    }

    /**
     * 切换轨迹跟踪开关
     */
    fun toggleTrace() {
        if (traceUseCase.isTracing()) {
            traceUseCase.stopTrace()
            // 停止轨迹跟踪后，提示保存路径
            _uiEvent.value = ShowSaveConfirmDialog
            // TODO: 路径缓存在本地，防止未保存等情况就直接关闭app丢失数据情况
        } else {
            traceUseCase.startTrace()
        }
        _isTracing.value = traceUseCase.isTracing()
    }

    /**
     * 切换跟踪模式
     */
    fun toggleFollowingMode() {
        val old = _isFollowing.value!!
        _isFollowing.value = !old
    }

    /**
     * 保存当前轨迹
     */
    fun saveTrace() {
        commitData { traceUseCase.saveTraceRecord() }
    }

    /**
     * 放弃当前轨迹
     */
    fun abandonTrace() {
        traceUseCase.clearTrace()
        // TODO: 刷新地图上轨迹线
    }

    // 页面暂停时，无需刷新 uiState
    fun onPause() {
        traceUseCase.removeLocationSuccessListener(onLocationSuccess)
        traceUseCase.removeTraceSuccessListener(onTraceSuccess)
    }

    fun onResume() {
        // 可能其他地方有修改，再次打开时刷新
        _uiState.value = UiState(traceUseCase.getMyLocation())
        _isTracing.value = traceUseCase.isTracing()

        traceUseCase.addLocationSuccessListener(onLocationSuccess)
        traceUseCase.addTraceSuccessListener(onTraceSuccess)
    }

    private val onLocationSuccess: (location: TraceLocation) -> Unit = {
        _uiState.value = UiState(it)
    }

    private val onTraceSuccess: (allTracePointList: ArrayList<TraceLocation>) -> Unit = {
        // 轨迹成功回调，回调的是整个轨迹列表
        _tracePointListUiState.value = it
    }

}