package com.boredream.lovebook.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.BaseViewModel
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel() {

    private val _uiState = SingleLiveEvent<SplashUiState>()
    val uiState: LiveData<SplashUiState> = _uiState

    fun autoLogin() {
        viewModelScope.launch {

            // 先尝试使用本地token登录
            repository.getLocalToken()?.let {
                try {
                    val response = repository.getUserInfo()
                    if (response.isSuccess()) {
                        // 登录成功跳转到主页
                        _uiState.value = AutoLoginSuccess
                        return@launch
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // 其它情况，跳转到登录页
            _uiState.value = AutoLoginFail
        }
    }
}
