package com.boredream.lovebook.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<BD : ViewDataBinding>(var binding: BD) :
    RecyclerView.ViewHolder(binding.root)