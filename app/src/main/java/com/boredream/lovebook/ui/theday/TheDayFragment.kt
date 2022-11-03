package com.boredream.lovebook.ui.theday

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.databinding.FragmentTheDayBinding
import com.boredream.lovebook.ui.BaseFragment
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

        viewModel.showPickDayState.observe(viewLifecycleOwner) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val dialog = DatePickerDialog(requireContext())
                dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                    viewModel.setTogetherDay(String.format("%d-%d-%d", year, month + 1, dayOfMonth))
                }
                dialog.show()
            }
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