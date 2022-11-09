package com.boredream.lovebook.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.data.event.DataUpdateEvent
import org.greenrobot.eventbus.EventBus


abstract class BaseRequestActivity<DATA, VM : BaseRequestViewModel<DATA>, BD : ViewDataBinding> :
    BaseActivity<VM, BD>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.commitDataUiState.observe(this) {
            when (it) {
                is SimpleRequestSuccess -> {
                    ToastUtils.showShort("提交成功")
                    finish()
                    // TODO: 发送event让其他地方更新，有repo后如何处理更优雅？
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }

    }

}