package com.boredream.lovebook.ui.trace.recorddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.TraceRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TraceRecordDetailViewModel @Inject constructor(
    private val repository: TraceRecordRepository
) : BaseRequestViewModel<TraceRecord>() {

    private val _traceListUiState = MutableLiveData(ArrayList<ArrayList<TraceLocation>>())
    val traceListUiState: LiveData<ArrayList<ArrayList<TraceLocation>>> = _traceListUiState

    private val _startLocationUiState = MutableLiveData<TraceLocation>()
    val startLocationUiState: LiveData<TraceLocation> = _startLocationUiState

    /**
     * 页面开始时，绘制路线，并跳转到对应位置
     */
    fun start(data: TraceRecord) {
        _traceListUiState.value = arrayListOf(data.traceList)

        // TODO:  根据路线，选择合适的 camera zoom 和 position
        _startLocationUiState.value = data.traceList[0]
    }

}