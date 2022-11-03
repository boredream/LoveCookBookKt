package com.boredream.lovebook.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.BaseUiState
import com.boredream.lovebook.ui.BaseViewModel
import com.boredream.lovebook.utils.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _uiState = MutableLiveData<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState

    private var fetchJob: Job? = null

    /**
     * 登陆
     */
    fun login() {
        Log.i("DDD", "login")
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val loginResponse = repository.login(username.value ?: "", password.value ?: "")

                // TODO isSuccess 判断的封装
                if (loginResponse.isSuccess()) {
                    // 登录成功，保存token，继续获取用户信息

                    val userInfoResponse = repository.getUserInfo()
                    if (userInfoResponse.isSuccess()) {
                        // 获取信息获取成功，完成登录
                        Log.i("DDD", "login success")
                        _uiState.value = LoginSuccess
                    } else {
                        requestError(userInfoResponse.msg)
                    }
                } else {
                    requestError(loginResponse.msg)
                }
            } catch (e: Exception) {
                requestError(e.message ?: "请求错误 $e")
            }
            _baseUiState.value = BaseUiState(showLoading = false)
        }
    }

    /**
     * 请求失败
     */
    private fun requestError(reason: String) {
        _uiState.value = LoginFail(reason)
    }
}