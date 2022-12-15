package com.boredream.lovebook.ui.thedaydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseViewModel() {

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _uiState = MutableLiveData<TheDay>()
    val uiState: LiveData<TheDay> = _uiState

    fun load(data: TheDay?) {
        // 默认今天
        _uiState.value = data ?: TheDay("", TimeUtils.date2String(Date(), "yyyy-MM-dd"))
    }

    fun commit() {
        val theDay = _uiState.value!!

        if (StringUtils.isEmpty(theDay.name)) {
            _baseEvent.value = ToastLiveEvent("名字不能为空")
            return
        }
        if (StringUtils.isEmpty(theDay.theDayDate)) {
            _baseEvent.value = ToastLiveEvent("日期不能为空")
            return
        }

        if (theDay.id != null) {
            commitVMCompose.request(
                onSuccess = { _baseEvent.value = ToastLiveEvent("修改成功") },
                repoRequest = { repository.update(theDay) }
            )
        } else {
            commitVMCompose.request(
                onSuccess = { _baseEvent.value = ToastLiveEvent("新增成功") },
                repoRequest = { repository.add(theDay) }
            )
        }
    }

}