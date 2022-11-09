package com.boredream.lovebook.ui.todogroup

import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.base.StartActivityLiveEvent
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoRepository
import com.boredream.lovebook.ui.todogroupdetail.TodoGroupDetailActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoGroupViewModel @Inject constructor(private val repository: TodoRepository) :
    BaseRequestViewModel<TodoGroup>() {

    fun loadList() {
        loadList { repository.getGroupList() }
    }

    fun startAdd() {
        _baseEvent.value = StartActivityLiveEvent(TodoGroupDetailActivity::class.java)
    }

    fun delete(data: TodoGroup) {
        commitData { repository.deleteGroup(data.id!!) }
    }

}