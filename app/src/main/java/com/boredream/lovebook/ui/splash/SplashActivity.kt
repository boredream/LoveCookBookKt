package com.boredream.lovebook.ui.splash

import android.os.Bundle
import android.widget.Toast
import com.boredream.lovebook.ui.main.MainActivity
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.databinding.ActivitySplashBinding
import com.boredream.lovebook.ui.BaseActivity
import com.boredream.lovebook.ui.login.LoginActivity
import com.boredream.lovebook.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    // TODO: The application should not provide its own launch screen

    override fun getLayoutId() = R.layout.activity_splash

    override fun getViewModelClass() = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uiState.observe(this@SplashActivity) {
            when(it) {
                is AutoLoginSuccess -> MainActivity.start(this)
                is AutoLoginFail -> LoginActivity.start(this)
            }
            finish()
        }
        viewModel.autoLogin()
    }

}