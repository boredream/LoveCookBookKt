package com.boredream.lovebook.ui.thedaydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.*
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseViewModel() {

    private var fetchJob: Job? = null

    private val _uiState = MutableLiveData<TheDay>()
    val uiState: LiveData<TheDay> = _uiState

    private val _commitUiState = MutableLiveData<SimpleRequestUiState<TheDay>>()
    val commitUiState: LiveData<SimpleRequestUiState<TheDay>> = _commitUiState

    fun load(theDay: TheDay?) {
        var data = theDay
        if (data == null) {
            // TODO: 更好的写法？
            // 因为数据是双向绑定的，所以新建时要创建空对象，且和视图绑定的变量需要赋值
            data = TheDay()
            data.name = ""
            // 默认今天
            data.theDayDate = TimeUtils.date2String(Date(), "yyyy-MM-dd")
        }
        _uiState.value = data!!
    }

    fun commit() {
        Log.i("DDD", "login")

        val theDay = _uiState.value!!

        if (StringUtils.isEmpty(theDay.name)) {
            _commitUiState.value = SimpleRequestFail("名字不能为空")
            return
        }
        if (StringUtils.isEmpty(theDay.theDayDate)) {
            _commitUiState.value = SimpleRequestFail("日期不能为空")
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
                    _commitUiState.value = SimpleRequestSuccess(theDay)
                } else {
                    _commitUiState.value = SimpleRequestFail(response.msg)
                }
            } catch (e: Exception) {
                _commitUiState.value = SimpleRequestFail(e.message ?: "请求错误 $e")
            }
            _baseUiState.value = BaseUiState(showLoading = false)
        }
    }

}