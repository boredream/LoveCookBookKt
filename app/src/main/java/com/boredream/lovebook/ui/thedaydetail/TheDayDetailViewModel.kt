package com.boredream.lovebook.ui.thedaydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.base.BaseUiState
import com.boredream.lovebook.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _uiState = MutableLiveData<TheDay>()
    val uiState: LiveData<TheDay> = _uiState

    private val _commitUiState = MutableLiveData<TheDayDetailCommitUiState>()
    val commitUiState: LiveData<TheDayDetailCommitUiState> = _commitUiState

    fun load(theDay: TheDay?) {
        var data = theDay
        if (data == null) {
            // TODO: 更好的写法？
            // 因为数据是双向绑定的，所以新建时要创建空对象，且和视图绑定的变量需要赋值
            data = TheDay()
            data.name = ""
            data.theDayDate = ""
        }
        _uiState.value = data!!
    }

    fun commit() {
        Log.i("DDD", "login")

        val theDay = _uiState.value!!

        if (StringUtils.isEmpty(theDay.name)) {
            _commitUiState.value = CommitFail("名字不能为空")
            return
        }
        if (StringUtils.isEmpty(theDay.theDayDate)) {
            _commitUiState.value = CommitFail("日期不能为空")
            return
        }

        _baseUiState.value = BaseUiState(showLoading = true)
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response =
                    if (theDay.id != null) repository.update(theDay)
                    else repository.add(theDay)

                if (response.isSuccess()) {
                    _commitUiState.value = CommitSuccess
                } else {
                    requestError(response.msg)
                }
            } catch (e: Exception) {
                requestError(e.message ?: "请求错误 $e")
            }
            _baseUiState.value = BaseUiState(showLoading = false)
        }
    }

    /**
     * 请求失败
     */
    private fun requestError(reason: String) {
        _commitUiState.value = CommitFail(reason)
    }

}