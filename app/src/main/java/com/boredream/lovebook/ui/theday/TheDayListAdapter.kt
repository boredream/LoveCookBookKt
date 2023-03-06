package com.boredream.lovebook.ui.theday

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.ItemTheDayBinding
import java.util.*

class TheDayListAdapter(dataList: ArrayList<TheDay>) :
    BaseListAdapter<TheDay, ItemTheDayBinding>(dataList) {

    override fun getItemLayoutId() = R.layout.item_the_day

    override fun setItemData(binding: ItemTheDayBinding, data: TheDay) {
        val format = TimeUtils.getSafeDateFormat("yyyy-MM-dd")
        if (data.notifyType == TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN) {
            // 按年倒计天数
            val now = Calendar.getInstance().apply {
                // 清空时分秒
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val date = Calendar.getInstance().apply {
                timeInMillis = TimeUtils.string2Date(data.theDayDate, format).time
            }

            // 先设置成同一年
            date.set(Calendar.YEAR, now.get(Calendar.YEAR))
            // 如果目标时间早于当前时间，挪到下一年
            if (date.before(now)) {
                date.add(Calendar.YEAR, 1)
            }
            val span = TimeUtils.getTimeSpan(date.timeInMillis, now.timeInMillis, TimeConstants.DAY) + 1
            binding.tvNotifyPre.text = "还有"
            binding.tvNotifyDay.text = span.toString()
        } else {
            // 累计天数
            val span = -TimeUtils.getTimeSpanByNow(data.theDayDate, format, TimeConstants.DAY)
            binding.tvNotifyPre.text = "已经"
            binding.tvNotifyDay.text = span.toString()
        }
    }

}