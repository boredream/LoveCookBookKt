package com.boredream.lovebook.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.theday.TheDayRequestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MineViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<User>()
    val uiState: LiveData<User> = _uiState

    private val _requestUiState = MutableLiveData<TheDayRequestUiState>()
    val requestUiState: LiveData<TheDayRequestUiState> = _requestUiState

    fun loadUserInfo() {
        // 直接从本地取
        val user = repository.getLocalUser() ?: return
        _uiState.value = user
    }

}