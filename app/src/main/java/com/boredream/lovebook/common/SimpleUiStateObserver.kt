package com.boredream.lovebook.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.base.BaseView
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.ResponseEntity

/**
 * 简易UiState观察类，连接 BaseRequestViewModel 和 常用 UI
 */
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
        isRequestingObserver: Observer<Boolean> = Observer<Boolean> {
            // 默认发起和结束请求时，更新loading
            if (lifecycleOwner is BaseView) {
                lifecycleOwner.showLoading(it)
            }
        },
        successObserver: Observer<ResponseEntity<T>> = Observer<ResponseEntity<T>> {
            // 默认请求成功时，Toast
            ToastUtils.showShort("请求成功")
        },
        failObserver: Observer<ResponseEntity<T>> = Observer<ResponseEntity<T>> {
            // 默认请求失败时，Toast
            ToastUtils.showShort("请求失败 ${it.msg}")
        }
    ) {
        requestVMCompose.isRequestingUiState.observe(lifecycleOwner, isRequestingObserver)
        requestVMCompose.successUiState.observe(lifecycleOwner, successObserver)
        requestVMCompose.failUiState.observe(lifecycleOwner, failObserver)
    }

}

