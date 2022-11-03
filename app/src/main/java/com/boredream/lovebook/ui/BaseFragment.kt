package com.boredream.lovebook.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.BR


abstract class BaseFragment<VM: BaseViewModel, BD: ViewDataBinding>: Fragment() {

    // base
    protected lateinit var viewModel: VM
    private var binding: BD? = null
    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelClass(): Class<VM>

    fun getBinding(): BD {
        return binding!!
    }

    // view
    private lateinit var loadingDialog : ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        getBinding().lifecycleOwner = this
        getBinding().setVariable(BR.vm, viewModel)

        loadingDialog = ProgressDialog(activity)
        loadingDialog.setMessage("加载中...")
        viewModel.baseUiState.observe(viewLifecycleOwner) {
            if (it.showLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        viewModel.baseEvent.observe(viewLifecycleOwner) {
            when(it) {
                is StartActivityLiveEvent<*> -> startActivity(Intent(activity, it.activity))
                is ToastLiveEvent -> ToastUtils.showShort(it.toast)
            }
        }

        return getBinding().root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}