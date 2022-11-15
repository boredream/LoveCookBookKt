package com.boredream.lovebook.ui.todogroupdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTodoGroupDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoGroupDetailActivity :
    BaseActivity<TodoGroupDetailViewModel, ActivityTodoGroupDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_todo_group_detail

    override fun getViewModelClass() = TodoGroupDetailViewModel::class.java

    private var data: TodoGroup? = null

    companion object {
        fun start(context: Context, data: TodoGroup) {
            val intent = Intent(context, TodoGroupDetailActivity::class.java)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            data = it.getSerializable(BundleKey.DATA) as TodoGroup?
        }

        SimpleUiStateObserver.setCommitRequestObserver(viewModel, this)
        viewModel.load(data)
    }

}