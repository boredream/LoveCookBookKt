package com.boredream.lovebook.ui.theday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.common.SimpleListViewModel
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TheDayViewModel @Inject constructor(
    private val theDayRepository: TheDayRepository,
    private val userRepository: UserRepository,
) : SimpleListViewModel<TheDay>() {

    override fun isPageList() = true
    override suspend fun repoDeleteRequest(data: TheDay) = theDayRepository.delete(data.id!!)
    override suspend fun repoPageListRequest(
        loadMore: Boolean,
        forceRemote: Boolean
    ) = theDayRepository.getList(forceRemote)

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _headerUiState = MutableLiveData<TheDayUiState>()
    val headerUiState: LiveData<TheDayUiState> = _headerUiState

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
        _headerUiState.value = TheDayUiState(togetherDayTitle, togetherDay, leftAvatar, rightAvatar)
    }

    fun pickTogetherDay() {
        _showPickDayState.value = true
    }

    fun setTogetherDay(date: String) {
        println("TheDayViewModel setTogetherDay")

        commitVMCompose.request(
            onSuccess = { loadTogetherInfo() }
        ) { userRepository.updateTogetherDay(date) }
    }

}