package com.boredream.lovebook.ui.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.common.SimpleRequestSuccess
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.FragmentDiaryBinding
import com.boredream.lovebook.databinding.ItemDiaryBinding
import com.boredream.lovebook.ui.diarydetail.DiaryDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiaryFragment : BaseFragment<DiaryViewModel, FragmentDiaryBinding>() {

    override fun getLayoutId() = R.layout.fragment_diary
    override fun getViewModelClass() = DiaryViewModel::class.java

    private var dataList = ArrayList<Diary>()
    private lateinit var adapter: SimpleListAdapter<Diary, ItemDiaryBinding>

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

    override fun onResume() {
        super.onResume()
        viewModel.start()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initList() {
        adapter = SimpleListAdapter(dataList, R.layout.item_diary)
        adapter.onItemClickListener = { DiaryDetailActivity.start(requireContext(), it) }
//        adapter.onItemLongClickListener = {
//            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.delete(it) })
//        }
        getBinding().refreshDiary.setup(
            adapter as BaseListAdapter<Any, ViewDataBinding>,
            onLoadMore = { viewModel.start(true) },
            onRefresh = { viewModel.start(false) },
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.toDetailEvent.observe(viewLifecycleOwner) {
            DiaryDetailActivity.start(requireContext())
        }

//        viewModel.commitDataUiState.observe(viewLifecycleOwner) {
//            when (it) {
//                is SimpleRequestSuccess -> {
//                    ToastUtils.showShort("删除成功")
//                    viewModel.start()
//                }
//                is SimpleRequestFail -> ToastUtils.showShort(it.reason)
//            }
//        }
    }

}