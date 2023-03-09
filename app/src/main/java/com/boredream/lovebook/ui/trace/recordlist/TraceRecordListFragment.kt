package com.boredream.lovebook.ui.trace.recordlist

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.databinding.FragmentTraceRecordListBinding
import com.boredream.lovebook.databinding.ItemTraceRecordBinding
import com.boredream.lovebook.service.SyncDataService
import com.boredream.lovebook.ui.trace.TraceMapActivity
import com.boredream.lovebook.ui.trace.recorddetail.TraceRecordDetailActivity
import com.boredream.lovebook.utils.DialogUtils
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.yanzhenjie.permission.AndPermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TraceRecordListFragment :
    BaseFragment<TraceRecordListViewModel, FragmentTraceRecordListBinding>() {

    override fun getLayoutId() = R.layout.fragment_trace_record_list
    override fun getViewModelClass() = TraceRecordListViewModel::class.java

    private var dataList = ArrayList<TraceRecord>()
    private lateinit var adapter: SimpleListAdapter<TraceRecord, ItemTraceRecordBinding>

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
        SyncDataService.startSync(requireContext())
    }

    private fun initList() {
        adapter = SimpleListAdapter(dataList, R.layout.item_trace_record)
        adapter.onItemClickListener = { TraceRecordDetailActivity.start(requireContext(), it) }
        adapter.onItemLongClickListener = {
            DialogUtils.showDeleteConfirmDialog(requireContext(), { viewModel.delete(it) })
        }
        getBinding().refreshTraceList.setup(
            adapter,
            enableRefresh = false,
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.toDetailEvent.observe(viewLifecycleOwner) { toDetail() }
        SimpleUiStateObserver.setRequestObserver(this, this, viewModel.deleteVMCompose) {
            // 提交成功后，开始推送信息
            SyncDataService.startPush(requireContext())
        }
    }

    private fun toDetail() {
        AndPermission.with(context)
            .runtime()
            .permission(Manifest.permission.ACCESS_FINE_LOCATION)
            .onGranted {
                TraceMapActivity.start(requireContext())
            }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                    PermissionSettingUtil.showSetting(requireContext(), permissions)
                }
            }
            .start()
    }
}