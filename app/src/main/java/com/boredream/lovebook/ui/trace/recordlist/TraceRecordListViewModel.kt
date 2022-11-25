package com.boredream.lovebook.ui.trace.recordlist

import androidx.lifecycle.LiveData
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TraceRecordListViewModel @Inject constructor(
    private val repository: TraceRecordRepository,
) : BaseRequestViewModel<TraceRecord>() {

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    init {
        // vm(页面) 创建时，清空缓存标志位，重新拉取接口
        repository.cacheIsDirty = true
    }

    fun start() {
        loadList { repository.getList() }
    }

    fun startAdd() {
        _toDetailEvent.value = true
    }

    fun delete(data: TraceRecord) {
        // commitData { repository.delete(data.id!!) }
    }

}