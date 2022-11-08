package com.boredream.lovebook.ui.theday

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.FragmentTheDayBinding
import com.boredream.lovebook.ui.thedaydetail.TheDayDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheDayFragment : BaseFragment<TheDayViewModel, FragmentTheDayBinding>() {

    override fun getLayoutId() = R.layout.fragment_the_day
    override fun getViewModelClass() = TheDayViewModel::class.java

    private var dataList = ArrayList<TheDay>()
    private lateinit var adapter: TheDayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        initList()
        initObserver()
        viewModel.loadTogetherInfo()
        viewModel.loadTheDayList()
        return view
    }

    private fun initList() {
        getBinding().rvTheDay.layoutManager = LinearLayoutManager(activity)
        adapter = TheDayListAdapter(dataList)
        adapter.onItemClickListener = { TheDayDetailActivity.start(requireContext(), it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.deleteTheDay(it) })
        }
        getBinding().rvTheDay.adapter = adapter
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
                is DeleteTheDaySuccess -> {
                    ToastUtils.showShort("删除成功")
                    viewModel.loadTheDayList()
                }
                is RequestFail -> ToastUtils.showShort(it.reason)
            }
        }

        viewModel.showPickDayState.observe(viewLifecycleOwner) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val dialog = DatePickerDialog(requireContext())
                dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    viewModel.setTogetherDay(String.format("%d-%d-%d", year, month + 1, dayOfMonth))
                }
                dialog.show()
            }
        }
    }

}