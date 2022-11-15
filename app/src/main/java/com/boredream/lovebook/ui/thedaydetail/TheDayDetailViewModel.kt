package com.boredream.lovebook.ui.thedaydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TheDayDetailViewModel @Inject constructor(
    private val repository: TheDayRepository
) : BaseRequestViewModel<TheDay>() {

    private val _uiState = MutableLiveData<TheDay>()
    val uiState: LiveData<TheDay> = _uiState

    fun load(data: TheDay?) {
        // 默认今天
        _uiState.value = data ?: TheDay("", TimeUtils.date2String(Date(), "yyyy-MM-dd"))
    }

    fun commit() {
        val theDay = _uiState.value!!

        if (StringUtils.isEmpty(theDay.name)) {
            _commitDataUiState.value = SimpleRequestFail("名字不能为空")
            return
        }
        if (StringUtils.isEmpty(theDay.theDayDate)) {
            _commitDataUiState.value = SimpleRequestFail("日期不能为空")
            return
        }

        commitData {
            if (theDay.id != null) repository.update(theDay)
            else repository.add(theDay)
        }
    }

}