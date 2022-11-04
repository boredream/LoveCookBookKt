package com.boredream.lovebook.ui.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.FragmentDiaryBinding
import com.boredream.lovebook.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiaryFragment : BaseFragment<DiaryViewModel, FragmentDiaryBinding>() {

    override fun getLayoutId() = R.layout.fragment_diary
    override fun getViewModelClass() = DiaryViewModel::class.java

    private var dataList = ArrayList<Diary>()
    private lateinit var adapter : DiaryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        getBinding().rvDiary.layoutManager = LinearLayoutManager(activity)
        adapter = DiaryListAdapter(dataList)
        getBinding().rvDiary.adapter = adapter

        viewModel.dataList.observe(viewLifecycleOwner) {
            // TODO: 使用Paging
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyItemRangeChanged(0, it.size)
        }

        viewModel.loadList()
        return view
    }

}