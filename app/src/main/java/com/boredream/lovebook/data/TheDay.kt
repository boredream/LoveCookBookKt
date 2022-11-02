package com.boredream.lovebook.data

class TheDay : Belong2UserEntity() {

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

    var name: String? = null
    var theDayDate: String? = null
    var notifyType = NOTIFY_TYPE_TOTAL_COUNT

}
