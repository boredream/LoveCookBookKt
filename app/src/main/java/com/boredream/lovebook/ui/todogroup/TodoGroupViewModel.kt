package com.boredream.lovebook.ui.todogroup

import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoGroupViewModel @Inject constructor(private val repository: TodoRepository) :
    BaseRequestViewModel<TodoGroup>() {

    fun loadList() {
        loadList {
            repository.getGroupList()
        }
    }

    fun startAddData() {

    }

    fun addData(todoGroup: TodoGroup) {
        commitData {
            repository.addGroup(todoGroup)
        }
    }

    fun deleteData(todoGroup: TodoGroup) {
        commitData {
            repository.addGroup(todoGroup)
        }
    }

}