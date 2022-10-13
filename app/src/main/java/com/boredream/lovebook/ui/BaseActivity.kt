package com.boredream.lovebook.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.boredream.lovebook.BR


abstract class BaseActivity<VM: BaseViewModel, BD: ViewDataBinding>: AppCompatActivity() {

    protected var mViewModel: VM? = null
    protected var mBinding: BD? = null

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewModel = ViewModelProvider(this).get(getViewModelClass())
        // mBinding?.setLifecycleOwner(this)
        // mBinding?.setVariable(BR.model, mViewModel)
    }

}