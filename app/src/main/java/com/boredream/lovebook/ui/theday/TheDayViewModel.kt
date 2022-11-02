package com.boredream.lovebook.ui.theday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import com.boredream.lovebook.ui.StartActivityLiveEvent
import com.boredream.lovebook.ui.thedaydetail.TheDayDetailActivity
import com.boredream.lovebook.vm.SingleLiveEvent
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

    private val _showPickDayState = SingleLiveEvent<Boolean>()
    val showPickDayState: LiveData<Boolean> = _showPickDayState

    private val _dataList = MutableLiveData<List<TheDay>>()
    val dataList: LiveData<List<TheDay>> = _dataList

    fun loadTogetherInfo() {
        // 直接从本地取
        val user = userRepository.getLocalUser() ?: return
        val togetherDayTitle: String
        val togetherDay: String
        if(user.cpTogetherDate != null) {
            togetherDayTitle = "我们已恋爱"
            val span = -TimeUtils.getTimeSpanByNow(user.cpTogetherDate,
                TimeUtils.getSafeDateFormat("yyyy-MM-dd"),
                TimeConstants.DAY)
            togetherDay = span.toString()
        } else {
            togetherDayTitle = "点我设置"
            togetherDay = "0"
        }
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
            if (response.isSuccess()) {
                // 请求成功后，刷新信息
                loadTogetherInfo()
            } else {
                requestError(response)
            }
        }
    }

    fun addTheDay() {
        _baseEvent.value = StartActivityLiveEvent(TheDayDetailActivity::class.java)
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