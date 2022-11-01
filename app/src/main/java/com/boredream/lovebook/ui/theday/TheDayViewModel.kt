package com.boredream.lovebook.ui.theday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TheDayViewModel @Inject constructor(
    private val theDayRepository: TheDayRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _uiState = MutableLiveData<TheDayUiState>()
    val uiState: LiveData<TheDayUiState> = _uiState

    private val _showPickDayState = MutableLiveData<Boolean>()
    val showPickDayState: LiveData<Boolean> = _showPickDayState

    private val _dataList = MutableLiveData<List<TheDay>>()
    val dataList: LiveData<List<TheDay>> = _dataList

    fun loadTogetherInfo() {
        // 直接从本地取
        val user = userRepository.getLocalUser() ?: return
        val togetherDayTitle = if(user.cpUser != null) "我们已恋爱" else "点我设置"
        val togetherDay = user.cpTogetherDate ?: "0"
        val leftAvatar = user.avatar
        val rightAvatar = user.cpUser?.avatar
        _uiState.value = TheDayUiState(togetherDayTitle, togetherDay, leftAvatar, rightAvatar)
    }

    fun pickTogetherDay() {
        _showPickDayState.value = true
    }

    fun setTogetherDay(date: String) {
        Log.i("DDD", "TheDayViewModel setTogetherDay")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response = userRepository.updateTogetherDay(date)
            _baseUiState.value = BaseUiState(showLoading = false)

            // TODO:
            if (response.isSuccess()) {

            } else {
                requestError(response)
            }
        }
    }

    fun loadTheDayList() {
        Log.i("DDD", "TheDayViewModel loadData")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response = theDayRepository.getList()
            _baseUiState.value = BaseUiState(showLoading = false)

            if (response.isSuccess()) {
                _dataList.value = response.data?.records
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