package com.boredream.lovebook.ui.todogroup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.common.SimpleRequestSuccess
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.databinding.FragmentTodoGroupBinding
import com.boredream.lovebook.databinding.ItemTodoGroupBinding
import com.boredream.lovebook.ui.diarydetail.DiaryDetailActivity
import com.boredream.lovebook.ui.todogroupdetail.TodoGroupDetailActivity
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
        adapter = SimpleListAdapter(dataList, R.layout.item_todo_group)
        adapter.onItemClickListener = { TodoListActivity.start(requireContext(), it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.delete(it) })
        }
        getBinding().refresh.setup(
            adapter,
            onRefresh = { viewModel.refresh(false) },
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.toDetailEvent.observe(viewLifecycleOwner) {
            TodoGroupDetailActivity.start(requireContext())
        }
        SimpleUiStateObserver.setRequestObserver(this, viewLifecycleOwner, viewModel.deleteVMCompose)
    }

}