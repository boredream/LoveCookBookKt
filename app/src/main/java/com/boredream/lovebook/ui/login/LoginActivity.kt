package com.boredream.lovebook.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.boredream.lovebook.MainActivity
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loginUiState.observe(this@LoginActivity) {
            if(it.isLoginSuccess) {
                MainActivity.start(this@LoginActivity, MainActivity::class.java);
            } else if(it.errorTip != null) {
                Toast.makeText(this, "请求失败 " + it.errorTip, Toast.LENGTH_LONG).show();
            }
        }

        viewModel.username.value = "18501683421"
        viewModel.password.value = "123456"
    }

}