package com.boredream.lovebook.ui.todogroupdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoGroupDetailViewModel @Inject constructor(
    private val repository: TodoGroupRepository
) : BaseRequestViewModel<TodoGroup>() {

    private val _uiState = MutableLiveData<TodoGroup>()
    val uiState: LiveData<TodoGroup> = _uiState

    fun load(data: TodoGroup?) {
        _uiState.value = data ?: TodoGroup("")
    }

    fun commit() {
        val data = _uiState.value!!

        if (StringUtils.isEmpty(data.name)) {
            _baseEvent.value = ToastLiveEvent("名字不能为空")
            return
        }

        commitData {
            if (data.id != null) repository.update(data)
            else repository.add(data)
        }
    }

}