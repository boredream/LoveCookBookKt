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
import com.boredream.lovebook.ui.FinishSelfActivityLiveEvent
import com.boredream.lovebook.ui.ToastLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseViewModel() {

    private var fetchJob: Job? = null

    // TODO: 可变的 不可变的 分开UIState

    private val _uiState = MutableLiveData<TheDay>()
    val uiState: LiveData<TheDay> = _uiState

    fun load(theDay: TheDay?) {
        theDay?.let {
            _uiState.value = it
        }
    }

    fun commit(notifyTypeTitle: String) {
        Log.i("DDD", "login")
        _baseUiState.value = BaseUiState(showLoading = true)

        // TODO: validate

        val theDay = _uiState.value ?: TheDay()
        theDay.notifyType = if ("累计天数" == notifyTypeTitle)
            TheDay.NOTIFY_TYPE_TOTAL_COUNT else TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val response =
                if (theDay.id != null) repository.update(theDay)
                else repository.add(theDay)
            _baseUiState.value = BaseUiState(showLoading = false)

            if (response.isSuccess()) {
                // TODO: 我这里care成功后的操作细节吗？
                _baseEvent.value = ToastLiveEvent("提交成功")
                _baseEvent.value = FinishSelfActivityLiveEvent()
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