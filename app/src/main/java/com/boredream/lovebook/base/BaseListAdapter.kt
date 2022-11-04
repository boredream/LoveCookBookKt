package com.boredream.lovebook.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.boredream.lovebook.BR
import com.boredream.lovebook.listener.OnCall

abstract class BaseListAdapter<T, BD: ViewDataBinding>(private val dataList: ArrayList<T>)
    : RecyclerView.Adapter<BaseListAdapter.BindingHolder>() {

    var onItemClickListener: OnCall<T>? = null
    var onItemLongClickListener: OnCall<T>? = null

    protected abstract fun getItemLayoutId(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: BD = DataBindingUtil.inflate(inflater, getItemLayoutId(), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = dataList[position]
        holder.binding.setVariable(BR.bean, data)

        holder.itemView.setOnClickListener {
            onItemClickListener?.call(data)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.call(data)
            true
        }
    }

    override fun getItemCount() = dataList.size

    class BindingHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

}