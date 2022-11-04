package com.boredream.lovebook.ui.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.FragmentDiaryBinding
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.base.SimpleListAdapter
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.ItemDiaryBinding
import com.boredream.lovebook.listener.OnCall
import com.boredream.lovebook.ui.thedaydetail.TheDayDetailActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiaryFragment : BaseFragment<DiaryViewModel, FragmentDiaryBinding>() {

    override fun getLayoutId() = R.layout.fragment_diary
    override fun getViewModelClass() = DiaryViewModel::class.java

    private var dataList = ArrayList<Diary>()
    private lateinit var adapter : SimpleListAdapter<Diary, ItemDiaryBinding>

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
        getBinding().rvDiary.layoutManager = LinearLayoutManager(activity)
        adapter = SimpleListAdapter(dataList, R.layout.item_diary)
        adapter.onItemClickListener = {
            // TODO:  
        }
        getBinding().rvDiary.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.dataList.observe(viewLifecycleOwner) {
            // TODO: 使用Paging
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

}