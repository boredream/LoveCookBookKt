package com.boredream.lovebook.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.boredream.lovebook.BR
import com.boredream.lovebook.MainActivity


abstract class BaseActivity<VM: BaseViewModel, BD: ViewDataBinding>: AppCompatActivity() {

    // base
    protected lateinit var viewModel: VM
    private lateinit var binding: BD
    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelClass(): Class<VM>

    // view
    private lateinit var loadingDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)

        loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("加载中...")
        viewModel.baseUiState.observe(this) {
            // TODO dialog ?
            if (it.showLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

}