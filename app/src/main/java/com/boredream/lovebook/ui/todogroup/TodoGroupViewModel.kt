package com.boredream.lovebook.ui.todogroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseUiState
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoGroupViewModel @Inject constructor(private val repository: TodoRepository) :
    BaseViewModel() {

    private var fetchJob: Job? = null

    private val _requestUiState = MutableLiveData<TodoGroupUiState>()
    val requestUiState: LiveData<TodoGroupUiState> = _requestUiState

    fun loadList() {
        Log.i("DDD", "TodoGroupViewModel loadList")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response = repository.getGroupList()
                _baseUiState.value = BaseUiState(showLoading = false)

                if (response.isSuccess()) {
                    _requestUiState.value = LoadListSuccess(response.getSuccessData())
                } else {
                    _requestUiState.value = RequestFail(response.msg)
                }
            } catch (e: Exception) {
                _requestUiState.value = RequestFail(e.message ?: "请求错误 $e")
            }
        }
    }

}