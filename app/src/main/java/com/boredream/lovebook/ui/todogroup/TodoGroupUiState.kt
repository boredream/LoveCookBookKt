package com.boredream.lovebook.ui.todogroup

import com.boredream.lovebook.data.TodoGroup

sealed class TodoGroupUiState
data class LoadListSuccess(val list: List<TodoGroup>) : TodoGroupUiState()
// data class DeleteTheDaySuccess(val data: TodoGroup) : TodoGroupUiState()
data class RequestFail(val reason: String) : TodoGroupUiState()