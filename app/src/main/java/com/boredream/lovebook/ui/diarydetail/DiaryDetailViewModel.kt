package com.boredream.lovebook.ui.diarydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.repo.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val repository: DiaryRepository,
) : BaseViewModel() {

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _uiState = MutableLiveData<Diary>()
    val uiState: LiveData<Diary> = _uiState

    fun load(data: Diary?) {
        // 默认今天
        _uiState.value = data ?: Diary("", TimeUtils.date2String(Date(), "yyyy-MM-dd"))
    }

    fun commit() {
        val data = _uiState.value!!

        // commitVMCompose 只处理接口相关的；校验提示需要通过其他 LiveData
        if (StringUtils.isEmpty(data.content)) {
            _baseEvent.value = ToastLiveEvent("内容不能为空")
            return
        }
        if (StringUtils.isEmpty(data.diaryDate)) {
            _baseEvent.value = ToastLiveEvent("日期不能为空")
            return
        }

        commitVMCompose.request {
            if (data.id != null) repository.update(data)
            else repository.add(data)
        }
    }

}