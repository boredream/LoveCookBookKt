package com.boredream.lovebook.ui.theday

import com.boredream.lovebook.data.TheDay

// TODO: 应该这么合并嘛？
sealed class TheDayRequestUiState
data class LoadListSuccess(val list: List<TheDay>) : TheDayRequestUiState()
data class DeleteTheDaySuccess(val theDay: TheDay) : TheDayRequestUiState()
data class RequestFail(val reason: String) : TheDayRequestUiState()