package com.boredream.lovebook.ui.theday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.source.TheDayRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TheDayViewModel @Inject constructor(private val repository: TheDayRepository) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _uiState = MutableLiveData<TheDayUiState>()
    val uiState: LiveData<TheDayUiState> = _uiState

    private val _dataList = MutableLiveData<List<TheDay>>()
    val dataList: LiveData<List<TheDay>> = _dataList

    fun loadTogetherInfo() {
        _uiState.value = TheDayUiState()
    }

    fun loadTheDayList() {
        Log.i("DDD", "TheDayViewModel loadData")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response = repository.getList()
            _baseUiState.value = BaseUiState(showLoading = false)

            if (response.isSuccess()) {
                _dataList.value = response.data.records
            } else {
                requestError(response)
            }
        }
    }

    /**
     * 请求失败
     */
    private fun <T> requestError(response: ResponseEntity<T>) {

    }

}