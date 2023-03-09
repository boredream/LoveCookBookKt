package com.boredream.lovebook.ui.trace.recordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseUiState
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
    val deleteVMCompose = RequestVMCompose<TraceRecord>(viewModelScope)

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    private val _isSyncingState = MutableLiveData<Boolean>()
    val isSyncingState: LiveData<Boolean> = _isSyncingState

    private val onSyncStatusChange: (status: Boolean) -> Unit = {
        _isSyncingState.value = it
    }

    fun start() {
        refreshListVMCompose.loadPageList(repoRequest = { repository.getList() })
    }

    fun onCreate() {
        repository.addStatusChangeListener(onSyncStatusChange)
    }

    fun onDestroy() {
        repository.removeStatusChangeListener(onSyncStatusChange)
    }

    fun startAdd() {
        _toDetailEvent.value = true
    }

    fun delete(data: TraceRecord) {
        deleteVMCompose.request(
            onSuccess = { start() }
        ) { repository.delete(data) }
    }

}