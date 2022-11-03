package com.boredream.lovebook.ui.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.boredream.lovebook.BR
import com.boredream.lovebook.R
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.ItemDiaryBinding

class DiaryListAdapter(private val dataList: ArrayList<Diary>) :
    RecyclerView.Adapter<DiaryListAdapter.BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemDiaryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_diary, parent, false
        )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = dataList[position]
        holder.binding.setVariable(BR.bean, data)
    }

    override fun getItemCount() = dataList.size

    class BindingHolder(var binding: ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root)

}