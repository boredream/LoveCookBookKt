package com.boredream.lovebook.ui.login

import android.os.Bundle
import android.widget.Toast
import com.boredream.lovebook.ui.main.MainActivity
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun getLayoutId() = R.layout.activity_login

    override fun getViewModelClass() = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uiState.observe(this@LoginActivity) {
            if (it.isLoginSuccess) {
                MainActivity.start(this@LoginActivity)
            } else if (it.errorTip != null) {
                Toast.makeText(this, "请求失败 " + it.errorTip, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.username.value = "18501683421"
        viewModel.password.value = "123456"
    }

}