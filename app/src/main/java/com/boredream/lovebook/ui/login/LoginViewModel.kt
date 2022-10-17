package com.boredream.lovebook.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.source.UserRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import com.boredream.lovebook.utils.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState: LiveData<LoginUiState> = _loginUiState

    private var fetchJob: Job? = null

    /**
     * 登陆
     */
    fun login() {
        Log.i("DDD", "login")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            // TODO 协程的错误处理
            val loginResponse = repository.login(username.value ?: "", password.value ?: "")
            _baseUiState.value = BaseUiState(showLoading = false)

            // TODO isSuccess 判断的封装
            if (loginResponse.isSuccess()) {
                // 登录成功，保存token，继续获取用户信息
                DataStoreUtils.putData("token", loginResponse.data)
                val userInfoResponse = repository.getUserInfo()
                if (userInfoResponse.isSuccess()) {
                    // 获取信息获取成功，完成登录
                    Log.i("DDD", "login success")
                    _loginUiState.value = LoginUiState(isLoginSuccess = true)
                } else {
                    requestError(userInfoResponse)
                }
            } else {
                requestError(loginResponse)
            }
        }
    }

    /**
     * 请求失败
     */
    private fun <T> requestError(response: ResponseEntity<T>) {
        _loginUiState.value = LoginUiState(isLoginSuccess = false, errorTip = response.msg)
    }
}