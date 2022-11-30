package com.boredream.lovebook.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.ResponseEntity

object SimpleUiStateObserver {

    fun setCommitRequestObserver(
        viewModel: BaseRequestViewModel<*>,
        lifecycleOwner: LifecycleOwner
    ) {
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

    /**
     * 设置请求观察者。
     * 参数为默认方法，需要替换操作时，传入参数；需要追加操作时，在外部再次自行observe
     */
    fun <T> setRequestObserver(
        lifecycleOwner: LifecycleOwner,
        requestVMCompose: RequestVMCompose<T>,
        successObserver: Observer<ResponseEntity<T>> = Observer<ResponseEntity<T>> {
            ToastUtils.showShort("请求成功")
        },
        failObserver: Observer<ResponseEntity<T>> = Observer<ResponseEntity<T>> {
            ToastUtils.showShort("请求失败 ${it.msg}")
        }
    ) {
        requestVMCompose.successUiState.observe(lifecycleOwner, successObserver)
        requestVMCompose.failUiState.observe(lifecycleOwner, failObserver)
    }

}

