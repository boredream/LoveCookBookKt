package com.boredream.lovebook.data

data class TheDay(
    var name: String,
    var theDayDate: String,
    var notifyType: Int = NOTIFY_TYPE_TOTAL_COUNT
) : Belong2UserEntity() {

    companion object {

        /**
         * 提醒方式 累计天数
         */
        const val NOTIFY_TYPE_TOTAL_COUNT = 1

        /**
         * 提醒方式 按年倒计天数
         */
        const val NOTIFY_TYPE_YEAR_COUNT_DOWN = 2
    }

    override fun toString(): String {
        return "TheDay(name='$name', theDayDate='$theDayDate')"
    }

}
