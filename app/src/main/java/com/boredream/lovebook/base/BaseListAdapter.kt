package com.boredream.lovebook.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.boredream.lovebook.BR
import com.boredream.lovebook.common.BindingViewHolder

abstract class BaseListAdapter<T, BD : ViewDataBinding>(private val dataList: ArrayList<T>) :
    RecyclerView.Adapter<BindingViewHolder<BD>>() {

    var onItemClickListener: (t: T) -> Unit = { }
    var onItemLongClickListener: (t: T) -> Unit = { }

    protected abstract fun getItemLayoutId(): Int
    protected open fun setItemData(binding: BD, data: T) {
        // 大部分数据都MVVM了，这里负责额外处理
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<BD> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: BD = DataBindingUtil.inflate(inflater, getItemLayoutId(), parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<BD>, position: Int) {
        val data = dataList[position]
        holder.binding.setVariable(BR.bean, data)
        setItemData(holder.binding, data)

        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(data)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener.invoke(data)
            true
        }
    }

    override fun getItemCount() = dataList.size

}