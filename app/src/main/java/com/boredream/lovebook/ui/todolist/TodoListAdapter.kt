package com.boredream.lovebook.ui.todolist

import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.databinding.ItemTodoBinding

class TodoListAdapter(dataList: ArrayList<Todo>) :
    BaseListAdapter<Todo, ItemTodoBinding>(dataList) {

    var onTodoCheck: ((data: Todo) -> Unit)? = null

    override fun getItemLayoutId() = R.layout.item_todo

    override fun setItemData(binding: ItemTodoBinding, data: Todo) {
        binding.cbDone.setOnClickListener {
            data.done = binding.cbDone.isChecked
            onTodoCheck?.invoke(data)
        }
    }

}