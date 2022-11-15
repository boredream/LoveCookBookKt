package com.boredream.lovebook.common

import androidx.databinding.ViewDataBinding
import com.boredream.lovebook.base.BaseListAdapter

class SimpleListAdapter<T, BD : ViewDataBinding>(
    dataList: ArrayList<T>,
    private val itemLayoutId: Int
) :
    BaseListAdapter<T, BD>(dataList) {

    override fun getItemLayoutId() = itemLayoutId

}