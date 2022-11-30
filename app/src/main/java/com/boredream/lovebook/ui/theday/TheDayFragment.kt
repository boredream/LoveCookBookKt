package com.boredream.lovebook.ui.theday

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        viewModel.clearCache()
        viewModel.loadTogetherInfo()
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.start()
    }

    private fun initList() {
        adapter = TheDayListAdapter(dataList)
        adapter.onItemClickListener = { TheDayDetailActivity.start(requireContext(), it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.delete(it) })
        }
        getBinding().refreshTheDay.setup(
            adapter,
            onRefresh = { viewModel.refresh() },
            itemDecoration = null
        )
    }

    private fun initObserver() {
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