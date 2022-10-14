package com.boredream.lovebook.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.boredream.lovebook.BR


abstract class BaseActivity<VM: BaseViewModel, BD: ViewDataBinding>: AppCompatActivity() {

    protected lateinit var viewModel: VM
    private lateinit var binding: BD

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)
    }

}