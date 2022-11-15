package com.boredream.lovebook.ui.tododetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTodoDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoDetailActivity : BaseActivity<TodoDetailViewModel, ActivityTodoDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_todo_detail

    override fun getViewModelClass() = TodoDetailViewModel::class.java

    private lateinit var groupId: String
    private var data: Todo? = null

    companion object {
        fun start(context: Context, groupId: String, data: Todo?) {
            val intent = Intent(context, TodoDetailActivity::class.java)
            intent.putExtra(BundleKey.ID, groupId)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            groupId = it.getString(BundleKey.ID, "")
            data = it.getSerializable(BundleKey.DATA) as Todo?
        }

        SimpleUiStateObserver.setCommitRequestObserver(viewModel, this)
        viewModel.load(groupId, data)
    }

}