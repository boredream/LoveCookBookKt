package com.boredream.lovebook.ui.login

import androidx.appcompat.app.AppCompatActivity
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.ui.BaseActivity

class LoginActivity: BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModelClass(): Class<LoginViewModel> {
        TODO("Not yet implemented")
    }


}