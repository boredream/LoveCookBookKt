package com.boredream.lovebook.ui.diary

import androidx.lifecycle.LiveData
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DiaryViewModel @Inject constructor(private val repository: DiaryRepository) :
    BaseRequestViewModel<Diary>() {

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    fun start(loadMore: Boolean = false) {
        loadList { repository.getPageList(loadMore) }
    }

    fun startAdd() {
        _toDetailEvent.value = true
    }

    fun delete(data: Diary) {
        commitData { repository.delete(data.id!!) }
    }

}