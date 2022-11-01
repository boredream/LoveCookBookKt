package com.boredream.lovebook.ui.theday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.FragmentTheDayBinding
import com.boredream.lovebook.ui.BaseFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheDayFragment : BaseFragment<TheDayViewModel, FragmentTheDayBinding>() {

    override fun getLayoutId() = R.layout.fragment_the_day
    override fun getViewModelClass() = TheDayViewModel::class.java

    private var dataList = ArrayList<TheDay>()
    private lateinit var adapter : TheDayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        getBinding().rvTheDay.layoutManager = LinearLayoutManager(activity)
        adapter = TheDayListAdapter(dataList)
        getBinding().rvTheDay.adapter = adapter

        viewModel.uiState.observe(viewLifecycleOwner) {
            // TODO: glide 如何 mvvm 优化
            Glide.with(this).load(it.leftAvatar).into(getBinding().ivLeft)
            Glide.with(this).load(it.rightAvatar).into(getBinding().ivRight)
        }
        viewModel.showPickDayState.observe(viewLifecycleOwner) {
            // TODO: show dialog
            viewModel.setTogetherDay("2022-12-21")
        }
        viewModel.dataList.observe(viewLifecycleOwner) {
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyItemRangeChanged(0, it.size)
        }

        viewModel.loadTogetherInfo()
        viewModel.loadTheDayList()
        return view
    }

}