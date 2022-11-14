package com.boredream.lovebook.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MineViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    private val _uiState = MutableLiveData<User>()
    val uiState: LiveData<User> = _uiState

    private val _eventUiState = SingleLiveEvent<MineEventState>()
    val eventUiState: LiveData<MineEventState> = _eventUiState

    fun loadUserInfo() {
        // 直接从本地取
        val user = repository.getLocalUser() ?: return
        _uiState.value = user
    }

    fun logout() {
        viewModelScope.launch {
            // FIXME: crash？ 
            repository.logout()
            _eventUiState.value = LogoutEvent
        }
    }

}

sealed class MineEventState
object LogoutEvent : MineEventState()
