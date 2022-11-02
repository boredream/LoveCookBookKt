package com.boredream.lovebook.ui.thedaydetail

import com.boredream.lovebook.data.TheDay

data class TheDayDetailUiState(
    val name: String? = null,
    val theDayDate: String? = null,
    val notifyType: Int = TheDay.NOTIFY_TYPE_TOTAL_COUNT,
)