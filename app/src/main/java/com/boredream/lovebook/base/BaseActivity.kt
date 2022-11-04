package com.boredream.lovebook.base

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.BR


abstract class BaseActivity<VM : BaseViewModel, BD : ViewDataBinding> : AppCompatActivity() {

    // base
    protected lateinit var viewModel: VM
    protected lateinit var binding: BD
    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelClass(): Class<VM>

    // view
    private lateinit var loadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)

        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("加载中...")

        // TODO: 和fragment合并
        viewModel.baseUiState.observe(this) {
            // TODO dialog ?
            if (it.showLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        viewModel.baseEvent.observe(this) {
            when(it) {
                is StartActivityLiveEvent<*> -> startActivity(Intent(this, it.activity))
                is FinishSelfActivityLiveEvent -> finish()
                is ToastLiveEvent -> ToastUtils.showShort(it.toast)
            }
        }

    }

}