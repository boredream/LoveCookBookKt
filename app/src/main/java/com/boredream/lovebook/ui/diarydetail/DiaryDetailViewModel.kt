package com.boredream.lovebook.ui.diarydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.base.SimpleRequestFail
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.repo.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    private val repository: DiaryRepository
) : BaseRequestViewModel<Diary>() {

    private val _uiState = MutableLiveData<Diary>()
    val uiState: LiveData<Diary> = _uiState

    fun load(data: Diary?) {
        // 默认今天
        _uiState.value = data ?: Diary("", TimeUtils.date2String(Date(), "yyyy-MM-dd"))
    }

    fun commit() {
        val data = _uiState.value!!

        if (StringUtils.isEmpty(data.content)) {
            _commitDataUiState.value = SimpleRequestFail("内容不能为空")
            return
        }
        if (StringUtils.isEmpty(data.diaryDate)) {
            _commitDataUiState.value = SimpleRequestFail("日期不能为空")
            return
        }

        commitData {
            if (data.id != null) repository.update(data)
            else repository.add(data)
        }
    }

}