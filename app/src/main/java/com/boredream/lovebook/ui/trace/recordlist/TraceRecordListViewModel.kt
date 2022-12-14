package com.boredream.lovebook.ui.trace.recordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.common.vmcompose.RefreshListVMCompose
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TraceRecordListViewModel @Inject constructor(
    private val repository: TraceRecordRepository,
) : BaseViewModel() {

    val refreshListVMCompose = RefreshListVMCompose(viewModelScope)
    val deleteVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    fun start() {
        refreshListVMCompose.loadPageList(repoRequest = { repository.getPageList(false) })
    }

    fun refresh(loadMore: Boolean, handlePullDownDown: Boolean = true) {
        refreshListVMCompose.loadPageList(
            handlePullDownDown = handlePullDownDown,
            repoRequest = { repository.getPageList(loadMore, true) })
    }

    fun startAdd() {
        _toDetailEvent.value = true
    }

    fun delete(data: TraceRecord) {
        deleteVMCompose.request(
            onSuccess = { refresh(false) }
        ) { repository.delete(data) }
    }

}