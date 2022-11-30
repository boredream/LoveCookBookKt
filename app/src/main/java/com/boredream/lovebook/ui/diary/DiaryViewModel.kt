package com.boredream.lovebook.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.common.vmcompose.RefreshListVMCompose
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: DiaryRepository
) : BaseViewModel() {

    val refreshListVMCompose = RefreshListVMCompose(viewModelScope)
    val deleteVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    fun start() {
        refreshListVMCompose.loadList(repoRequest = { repository.getPageList(false) })
    }

    fun refresh(loadMore: Boolean, handlePullDownDown: Boolean = true) {
        refreshListVMCompose.loadList(
            handlePullDownDown = handlePullDownDown,
            repoRequest = { repository.getPageList(loadMore, true) })
    }

    fun startAdd() {
        _toDetailEvent.value = true
    }

    fun delete(data: Diary) {
        deleteVMCompose.request(
            onSuccess = { refresh(loadMore = false, handlePullDownDown = false) },
            repoRequest = { repository.delete(data.id!!) })
    }

}