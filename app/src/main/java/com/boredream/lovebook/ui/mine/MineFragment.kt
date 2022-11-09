package com.boredream.lovebook.ui.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.base.SimpleListAdapter
import com.boredream.lovebook.data.SettingItem
import com.boredream.lovebook.databinding.FragmentMineBinding
import com.boredream.lovebook.databinding.ItemSettingBinding
import com.boredream.lovebook.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override fun getLayoutId() = R.layout.fragment_mine
    override fun getViewModelClass() = MineViewModel::class.java

    private var dataList = ArrayList<SettingItem>()
    private lateinit var adapter : SimpleListAdapter<SettingItem, ItemSettingBinding>

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
        getBinding().rvSetting.layoutManager = LinearLayoutManager(activity)
        dataList.add(SettingItem("另一半", "绑定"))
        dataList.add(SettingItem("关于我们", ""))
        dataList.add(SettingItem("推荐给好友", ""))
        dataList.add(SettingItem("意见反馈", ""))
        adapter = SimpleListAdapter(dataList, R.layout.item_setting)
        adapter.onItemClickListener = {
            when(it.name) {
                "另一半" -> toggleBindCp()
                // TODO: click
            }
        }
        getBinding().rvSetting.adapter = adapter
    }

    private fun toggleBindCp() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            // 更新用户绑定另一半信息
            val bindCpItem = dataList[0]
            bindCpItem.content = if(it.cpUser != null) ("昵称：" + it.cpUser?.nickname) else "点击绑定"
            adapter.notifyDataSetChanged()
        }

        viewModel.eventUiState.observe(viewLifecycleOwner) {
            when(it) {
                is LogoutEvent -> LoginActivity.start(requireContext())
            }
        }
    }

}