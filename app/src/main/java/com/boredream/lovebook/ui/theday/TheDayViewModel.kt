package com.boredream.lovebook.ui.theday

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.StartActivityLiveEvent
import com.boredream.lovebook.common.vmcompose.RefreshListVMCompose
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.thedaydetail.TheDayDetailActivity
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TheDayViewModel @Inject constructor(
    private val theDayRepository: TheDayRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)
    val refreshListVMCompose = RefreshListVMCompose(viewModelScope)

    private val _uiState = MutableLiveData<TheDayUiState>()
    val uiState: LiveData<TheDayUiState> = _uiState

    private val _showPickDayState = SingleLiveEvent<Boolean>()
    val showPickDayState: LiveData<Boolean> = _showPickDayState

    fun loadTogetherInfo() {
        // 直接从本地取
        val user = userRepository.getLocalUser() ?: return
        val togetherDayTitle: String
        val togetherDay: String
        if (user.cpTogetherDate != null) {
            togetherDayTitle = "我们已恋爱"
            val span = -TimeUtils.getTimeSpanByNow(
                user.cpTogetherDate,
                TimeUtils.getSafeDateFormat("yyyy-MM-dd"),
                TimeConstants.DAY
            )
            togetherDay = span.toString()
        } else {
            togetherDayTitle = "点我设置"
            togetherDay = "0"
        }
        val leftAvatar = user.avatar
        val rightAvatar = user.cpUser?.avatar
        _uiState.value = TheDayUiState(togetherDayTitle, togetherDay, leftAvatar, rightAvatar)
    }

    fun pickTogetherDay() {
        _showPickDayState.value = true
    }

    fun setTogetherDay(date: String) {
        println("TheDayViewModel setTogetherDay")

        commitVMCompose.request(
            onSuccess = { loadTogetherInfo() },
            repoRequest = { userRepository.updateTogetherDay(date) })
    }

    fun startAdd() {
        _baseEvent.value = StartActivityLiveEvent(TheDayDetailActivity::class.java)
    }

    fun delete(data: TheDay) {
        Log.i("DDD", "TheDayViewModel deleteTheDay ${data.name}")

        commitVMCompose.request(
            onSuccess = { refresh(false) },
            repoRequest = { theDayRepository.delete(data.id!!) })
    }

    fun start() {
        refreshListVMCompose.loadPageList(repoRequest = { theDayRepository.getList(false) })
    }

    fun refresh(handlePullDownDown: Boolean = true) {
        refreshListVMCompose.loadPageList(
            handlePullDownDown = handlePullDownDown,
            repoRequest = { theDayRepository.getList(true) })
    }

}