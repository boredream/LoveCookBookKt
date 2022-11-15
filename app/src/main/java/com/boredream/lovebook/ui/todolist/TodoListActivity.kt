package com.boredream.lovebook.ui.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.common.SimpleRequestSuccess
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTodoListBinding
import com.boredream.lovebook.databinding.ItemSettingBinding
import com.boredream.lovebook.ui.tododetail.TodoDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoListActivity : BaseActivity<TodoListViewModel, ActivityTodoListBinding>() {

    override fun getLayoutId() = R.layout.activity_todo_list
    override fun getViewModelClass() = TodoListViewModel::class.java

    private lateinit var data: TodoGroup
    private var dataList = ArrayList<Todo>()
    private lateinit var adapter: SimpleListAdapter<Todo, ItemSettingBinding>

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.start(data.id!!)
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
        SimpleUiStateObserver.setCommitRequestObserver(viewModel, this)

        viewModel.loadListUiState.observe(this) {
            when (it) {
                is SimpleRequestSuccess -> {
                    dataList.clear()
                    dataList.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }

        viewModel.toDetailEvent.observe(this) {
            TodoDetailActivity.start(this, data.id!!, null)
        }
    }

}