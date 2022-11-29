package com.boredream.lovebook.ui.todogroup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.common.SimpleRequestSuccess
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.databinding.FragmentTodoGroupBinding
import com.boredream.lovebook.databinding.ItemTodoGroupBinding
import com.boredream.lovebook.ui.todolist.TodoListActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoGroupFragment : BaseFragment<TodoGroupViewModel, FragmentTodoGroupBinding>() {

    override fun getLayoutId() = R.layout.fragment_todo_group
    override fun getViewModelClass() = TodoGroupViewModel::class.java

    private var dataList = ArrayList<TodoGroup>()
    private lateinit var adapter: SimpleListAdapter<TodoGroup, ItemTodoGroupBinding>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initList()
        initObserver()
        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }

    private fun initList() {
        getBinding().rv.layoutManager = LinearLayoutManager(activity)
        getBinding().rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = SimpleListAdapter(dataList, R.layout.item_todo_group)
        adapter.onItemClickListener = { TodoListActivity.start(requireContext(), it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.delete(it) })
        }
        getBinding().rv.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.loadListUiState.observe(viewLifecycleOwner) {
            when (it) {
                is SimpleRequestSuccess -> {
                    dataList.clear()
                    dataList.addAll(it.data.dataList)
                    adapter.notifyDataSetChanged()
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }
        viewModel.commitDataUiState.observe(viewLifecycleOwner) {
            when (it) {
                is SimpleRequestSuccess -> {
                    ToastUtils.showShort("删除成功")
                    viewModel.start()
                }
                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
            }
        }
    }

}