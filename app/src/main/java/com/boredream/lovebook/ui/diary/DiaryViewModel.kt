package com.boredream.lovebook.ui.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DiaryViewModel @Inject constructor(private val repository: DiaryRepository) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _dataList = MutableLiveData<ArrayList<Diary>>(ArrayList())
    val dataList: LiveData<ArrayList<Diary>> = _dataList

    private var page = 1

    fun loadList(loadMore: Boolean = false) {
        Log.i("DDD", "DiaryViewModel loadList")
        _baseUiState.value = BaseUiState(showLoading = true)

        val requestPage = if(loadMore) page + 1 else 1

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response = repository.getList(requestPage)
            _baseUiState.value = BaseUiState(showLoading = false)

            if (response.isSuccess()) {
                page = requestPage
                val list = _dataList.value ?: ArrayList()
                if(!loadMore) {
                    list.clear()
                }
                list.addAll(response.getSuccessData().records)
                _dataList.value = list
            } else {
                requestError(response)
            }
        }
    }

    /**
     * 请求失败
     */
    private fun <T> requestError(response: ResponseEntity<T>) {
        // TODO: toast?
    }

}