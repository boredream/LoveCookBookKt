package com.boredream.lovebook.ui.splash

import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel() {

    val loginVMCompose = RequestVMCompose<User>(viewModelScope)

    fun autoLogin() {
        loginVMCompose.request { repository.autoLogin() }
    }
}
