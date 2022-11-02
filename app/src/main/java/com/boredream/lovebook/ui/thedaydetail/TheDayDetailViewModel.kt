package com.boredream.lovebook.ui.thedaydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _uiState = MutableLiveData<TheDayDetailUiState>()
    val uiState: LiveData<TheDayDetailUiState> = _uiState

    private val _showPickDayState = SingleLiveEvent<Boolean>()
    val showPickDayState: LiveData<Boolean> = _showPickDayState

    fun load(theDay: TheDay?) {
        if (theDay == null) return

        // TODO: 初始化数据
        _uiState.value = TheDayDetailUiState(theDay.name, theDay.theDayDate)
    }

    fun commit() {
        Log.i("DDD", "login")
        _baseUiState.value = BaseUiState(showLoading = true)

        // TODO: validate
        val theDay = TheDay()

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response = repository.add(theDay)
            _baseUiState.value = BaseUiState(showLoading = false)

            if (response.isSuccess()) {
                // TODO: success
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