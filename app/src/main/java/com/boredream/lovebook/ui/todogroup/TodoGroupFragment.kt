package com.boredream.lovebook.ui.todogroup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.base.SimpleListAdapter
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.databinding.FragmentTodoGroupBinding
import com.boredream.lovebook.databinding.ItemTodoGroupBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoGroupFragment : BaseFragment<TodoGroupViewModel, FragmentTodoGroupBinding>() {

    override fun getLayoutId() = R.layout.fragment_todo_group
    override fun getViewModelClass() = TodoGroupViewModel::class.java

    private var dataList = ArrayList<TodoGroup>()
    private lateinit var adapter : SimpleListAdapter<TodoGroup, ItemTodoGroupBinding>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initList()
        initObserver()
        viewModel.loadList()
        return view
    }

    private fun initList() {
        getBinding().rv.layoutManager = LinearLayoutManager(activity)
        adapter = SimpleListAdapter(dataList, R.layout.item_diary)
        adapter.onItemClickListener = {
            // TODO:  
        }
        getBinding().rv.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.requestUiState.observe(viewLifecycleOwner) {
            when (it) {
                is LoadListSuccess -> {
                    dataList.clear()
                    dataList.addAll(it.list)
                    adapter.notifyDataSetChanged()
                }
                is RequestFail -> ToastUtils.showShort(it.reason)
            }
        }
    }

}