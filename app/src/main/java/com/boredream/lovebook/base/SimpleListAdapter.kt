package com.boredream.lovebook.base

import androidx.databinding.ViewDataBinding
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.ItemDiaryBinding

class SimpleListAdapter<T, BD : ViewDataBinding>(
    dataList: ArrayList<T>,
    private val itemLayoutId: Int
) :
    BaseListAdapter<T, BD>(dataList) {

    override fun getItemLayoutId() = itemLayoutId

}