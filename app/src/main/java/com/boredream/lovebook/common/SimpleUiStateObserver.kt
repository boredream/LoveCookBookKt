package com.boredream.lovebook.common

import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.base.BaseRequestViewModel

object SimpleUiStateObserver {

    fun setCommitRequestObserver(viewModel: BaseRequestViewModel<*>, activity: AppCompatActivity) {
        viewModel.commitDataUiState.observe(activity) {
            when (it) {
                is SimpleRequestSuccess -> {
                    ToastUtils.showShort("提交成功")
                    activity.finish()
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }
    }

}

