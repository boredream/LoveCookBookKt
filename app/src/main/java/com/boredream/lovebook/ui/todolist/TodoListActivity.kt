package com.boredream.lovebook.ui.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseRequestActivity
import com.boredream.lovebook.base.SimpleListAdapter
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTodoListBinding
import com.boredream.lovebook.databinding.ItemSettingBinding
import com.boredream.lovebook.ui.tododetail.TodoDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoListActivity : BaseRequestActivity<Todo, TodoListViewModel, ActivityTodoListBinding>() {

    override fun getLayoutId() = R.layout.activity_todo_list
    override fun getViewModelClass() = TodoListViewModel::class.java

    private lateinit var data: TodoGroup
    private var dataList = ArrayList<Todo>()
    private lateinit var adapter : SimpleListAdapter<Todo, ItemSettingBinding>

    companion object {
        fun start(context: Context, data: TodoGroup) {
            val intent = Intent(context, TodoListActivity::class.java)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            data = it.getSerializable(BundleKey.DATA) as TodoGroup
        }
        initList()
        initObserver()

        viewModel.loadList(data.id!!)
    }

    private fun initList() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = SimpleListAdapter(dataList, R.layout.item_todo)
        adapter.onItemClickListener = { TodoDetailActivity.start(this, data.id!!, it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(this, { viewModel.delete(it) })
        }
        binding.rv.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.toDetailEvent.observe(this) {
            TodoDetailActivity.start(this, data.id!!, null)
        }
    }

}