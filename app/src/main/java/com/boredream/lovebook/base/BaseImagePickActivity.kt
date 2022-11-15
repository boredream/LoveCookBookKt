package com.boredream.lovebook.base

import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.boredream.lovebook.listener.OnCall
import com.zhihu.matisse.Matisse

abstract class BaseImagePickActivity<VM : BaseViewModel, BD : ViewDataBinding> : BaseActivity<VM, BD>() {

    // TODO: 组合的方式替代继承？那activity result 如何抽取

    companion object {
        const val REQUEST_CODE_CHOOSE = 3009
    }

    var onImageResultCall : OnCall<List<Uri>>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            val result = Matisse.obtainResult(data);
            onImageResultCall?.call(result)
        }
    }

}