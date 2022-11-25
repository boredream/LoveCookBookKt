package com.boredream.lovebook.common

import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.base.BaseRequestViewModel

object SimpleUiStateObserver {

    fun setCommitRequestObserver(viewModel: BaseRequestViewModel<*>, lifecycleOwner: LifecycleOwner) {
        viewModel.commitDataUiState.observe(lifecycleOwner) {
            when (it) {
                is SimpleRequestSuccess -> {
                    ToastUtils.showShort("提交成功")

                    // TODO: 提交成功默认处理，关闭页面？
                    // activity.finish()
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }
    }

}

