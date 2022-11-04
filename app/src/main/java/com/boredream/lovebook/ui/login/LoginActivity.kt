package com.boredream.lovebook.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun getViewModelClass() = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uiState.observe(this@LoginActivity) {
            when (it) {
                is LoginSuccess -> {
                    MainActivity.start(this@LoginActivity)
                    finish()
                }
                is LoginFail -> ToastUtils.showShort(it.reason)
                is LoginValidateFail -> ToastUtils.showShort(it.reason)
            }
        }

        // FIXME: for test
        viewModel.username.value = "18501683422"
        viewModel.password.value = "123456"
    }

}