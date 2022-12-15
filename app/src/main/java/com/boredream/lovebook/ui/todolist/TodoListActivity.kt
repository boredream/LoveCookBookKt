package com.boredream.lovebook.ui.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTodoListBinding
import com.boredream.lovebook.ui.tododetail.TodoDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoListActivity : BaseActivity<TodoListViewModel, ActivityTodoListBinding>() {

    override fun getLayoutId() = R.layout.activity_todo_list
    override fun getViewModelClass() = TodoListViewModel::class.java

    private lateinit var data: TodoGroup
    private var dataList = ArrayList<Todo>()
    private lateinit var adapter: TodoListAdapter

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
        viewModel.todoGroup = data
        initList()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.start()
    }

    private fun initList() {
        adapter = TodoListAdapter(dataList)
        adapter.onItemClickListener = { TodoDetailActivity.start(this, data.id!!, it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(this, { viewModel.delete(it) })
        }
        binding.refresh.setup(adapter, onRefresh = { viewModel.refresh(false) })
        adapter.onTodoCheck = {
            viewModel.doneTodo(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.toDetailEvent.observe(this) {
            TodoDetailActivity.start(this, data.id!!)
        }
        SimpleUiStateObserver.setRequestObserver(this, this, viewModel.deleteVMCompose)
        // 修改完成状态希望无感知，所以默认回调都不需要
        // SimpleUiStateObserver.setRequestObserver(this, this, viewModel.updateVMCompose)
    }

}