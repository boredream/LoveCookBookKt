package com.boredream.lovebook.ui.tododetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseRequestViewModel<Todo>() {

    private val _uiState = MutableLiveData<Todo>()
    val uiState: LiveData<Todo> = _uiState

    fun load(groupId: String, data: Todo?) {
        _uiState.value = data ?: Todo(groupId, false, "", "", "", "")
    }

    fun commit() {
        val data = _uiState.value!!
        data.done = !StringUtils.isEmpty(data.doneDate)

        if (StringUtils.isEmpty(data.name)) {
            _commitDataUiState.value = SimpleRequestFail("名字不能为空")
            return
        }

        commitData {
            if (data.id != null) repository.update(data)
            else repository.add(data)
        }
    }

}