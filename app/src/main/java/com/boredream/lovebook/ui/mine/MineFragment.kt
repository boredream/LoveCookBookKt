package com.boredream.lovebook.ui.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.databinding.FragmentMineBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override fun getLayoutId() = R.layout.fragment_mine
    override fun getViewModelClass() = MineViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        initList()
        initObserver()
        viewModel.loadUserInfo()
        return view
    }

    private fun initList() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {

    }

}