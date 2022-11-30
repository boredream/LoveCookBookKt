package com.boredream.lovebook.ui.splash

import android.os.Bundle
import com.amap.api.mapcore.util.it
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.databinding.ActivitySplashBinding
import com.boredream.lovebook.ui.login.LoginActivity
import com.boredream.lovebook.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    // TODO: The application should not provide its own launch screen

    override fun getLayoutId() = R.layout.activity_splash

    override fun getViewModelClass() = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loginVMCompose.successUiState.observe(this) {
            MainActivity.start(this)
            finish()
        }
        viewModel.loginVMCompose.failUiState.observe(this) {
            LoginActivity.start(this)
            finish()
        }

        viewModel.autoLogin()
    }

}