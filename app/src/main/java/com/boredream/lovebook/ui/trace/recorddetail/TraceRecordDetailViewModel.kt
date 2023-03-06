package com.boredream.lovebook.ui.trace.recorddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.usecase.TraceDetailUseCase
import com.boredream.lovebook.utils.TraceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TraceRecordDetailViewModel @Inject constructor(
    private val useCase: TraceDetailUseCase
) : BaseViewModel() {

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)
    val requestVMCompose = RequestVMCompose<ArrayList<TraceLocation>>(viewModelScope)

    private val _uiState = MutableLiveData<TraceRecord>()
    val uiState: LiveData<TraceRecord> = _uiState

    private val _traceListUiState = MutableLiveData(ArrayList<ArrayList<TraceLocation>>())
    val traceListUiState: LiveData<ArrayList<ArrayList<TraceLocation>>> = _traceListUiState

    private val _startLocationUiState = MutableLiveData<TraceLocation>()
    val startLocationUiState: LiveData<TraceLocation> = _startLocationUiState

    private lateinit var data: TraceRecord

    /**
     * 页面开始时，绘制路线，并跳转到对应位置
     */
    fun start(data: TraceRecord) {
        this.data = data
        useCase.init(data.dbId)
        requestVMCompose.request(
            onSuccess = { getTraceListSuccess(it) },
            repoRequest = { useCase.getTraceList() })
    }

    fun commit() {
        val data = _uiState.value!!

        if (StringUtils.isEmpty(data.name)) {
            _baseEvent.value = ToastLiveEvent("名字不能为空")
            return
        }

        commitVMCompose.request { useCase.updateRecord(data) }
    }

    private fun getTraceListSuccess(traceList: ArrayList<TraceLocation>) {
        _uiState.value = data
        updateTraceList(traceList)
    }

    private fun updateTraceList(traceList: ArrayList<TraceLocation>) {
        _traceListUiState.value = arrayListOf(traceList)
        // TODO:  根据路线，选择合适的 camera zoom 和 position
        _startLocationUiState.value = traceList[traceList.lastIndex]
    }

}