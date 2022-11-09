package com.boredream.lovebook.ui.todogroup

import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.base.StartActivityLiveEvent
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoGroupRepository
import com.boredream.lovebook.ui.todogroupdetail.TodoGroupDetailActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoGroupViewModel @Inject constructor(private val repository: TodoGroupRepository) :
    BaseRequestViewModel<TodoGroup>() {

    fun start() {
        loadList { repository.getList() }
    }

    fun startAdd() {
        _baseEvent.value = StartActivityLiveEvent(TodoGroupDetailActivity::class.java)
    }

    fun delete(data: TodoGroup) {
        commitData { repository.delete(data.id!!) }
    }

}