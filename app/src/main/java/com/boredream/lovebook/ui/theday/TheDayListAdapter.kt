package com.boredream.lovebook.ui.theday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.BR
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.ItemTheDayBinding
import com.boredream.lovebook.ui.thedaydetail.TheDayDetailActivity
import java.util.*

class TheDayListAdapter(private val dataList: ArrayList<TheDay>)
    : RecyclerView.Adapter<TheDayListAdapter.BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemTheDayBinding = DataBindingUtil.inflate(inflater,
            R.layout.item_the_day, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = dataList[position]
        holder.binding.setVariable(BR.bean, data)
        if (data.notifyType == TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN) {
            // 按年倒计天数
            val now = Calendar.getInstance()
            val dateMillion = TimeUtils.string2Date(data.theDayDate, TimeUtils.getSafeDateFormat("yyyy-MM-dd")).time
            val date = Calendar.getInstance()
            date.timeInMillis = dateMillion

            // 先设置成同一年
            date.set(Calendar.YEAR, now.get(Calendar.YEAR))
            // 如果目标时间早于当前时间，挪到下一年
            if(date.before(now)) {
                date.add(Calendar.YEAR, 1)
            }
            val span = TimeUtils.getTimeSpanByNow(date.timeInMillis, TimeConstants.DAY)
            holder.binding.tvNotifyPre.text = "还有"
            holder.binding.tvNotifyDay.text = span.toString()
        } else {
            // 累计天数
            val span = -TimeUtils.getTimeSpanByNow(data.theDayDate,
                TimeUtils.getSafeDateFormat("yyyy-MM-dd"),
                TimeConstants.DAY)
            holder.binding.tvNotifyPre.text = "已经"
            holder.binding.tvNotifyDay.text = span.toString()
        }

        holder.itemView.setOnClickListener {
            TheDayDetailActivity.start(holder.itemView.context, data)
        }
    }

    override fun getItemCount() = dataList.size

    class BindingHolder(var binding: ItemTheDayBinding) : RecyclerView.ViewHolder(binding.root)

}