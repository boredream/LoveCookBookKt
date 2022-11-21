package com.boredream.lovebook.ui.splash

import android.Manifest
import android.os.Build
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.databinding.ActivitySplashBinding
import com.boredream.lovebook.ui.login.LoginActivity
import com.boredream.lovebook.ui.main.MainActivity
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.yanzhenjie.permission.AndPermission
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val requestPermissions = ArrayList<String>()
            requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            AndPermission.with(this)
                .runtime()
                .permission(requestPermissions.toTypedArray())
                .onGranted { autoLogin() }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this, requestPermissions)) {
                        PermissionSettingUtil.showSetting(this, permissions)
                    }
                }
                .start()
        } else {
            autoLogin()
        }
    }

    private fun autoLogin() {
        viewModel.autoLogin()
    }

}