package com.boredream.lovebook.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.base.RepoCacheHelper
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.databinding.ActivityLoginBinding
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

        // 登录时，清空原有所有缓存
        RepoCacheHelper.repoCache.clear()

        SimpleUiStateObserver.setRequestObserver(this, this,
            viewModel.loginVMCompose,
            successObserver = {
                MainActivity.start(this)
            })
    }

}