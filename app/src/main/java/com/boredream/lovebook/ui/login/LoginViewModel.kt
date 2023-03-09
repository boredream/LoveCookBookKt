package com.boredream.lovebook.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    val loginVMCompose = RequestVMCompose<User>(viewModelScope)

    // FIXME: remove me
    private val _uiState = MutableLiveData(LoginDto("18501683421", "123456"))
    val uiState: LiveData<LoginDto> = _uiState

    /**
     * 登陆
     */
    fun login() {
        val data = _uiState.value ?: LoginDto()

        if (StringUtils.isEmpty(data.username)) {
            _baseEvent.value = ToastLiveEvent("用户名不能为空")
            return
        }
        if (StringUtils.isEmpty(data.password)) {
            _baseEvent.value = ToastLiveEvent("密码不能为空")
            return
        }

        loginVMCompose.request { repository.login(data.username, data.password) }
    }
}