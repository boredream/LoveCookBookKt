package com.boredream.lovebook.ui.trace

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTraceMapBinding
import com.boredream.lovebook.service.SyncDataService
import com.boredream.lovebook.service.TraceLocationService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TraceMapActivity : BaseActivity<TraceMapViewModel, ActivityTraceMapBinding>() {

    private lateinit var serviceIntent: Intent

    override fun getLayoutId() = R.layout.activity_trace_map

    override fun getViewModelClass() = TraceMapViewModel::class.java

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, TraceMapActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        initObserver()
        toggleLocation(true)
    }

    private fun initObserver() {
        viewModel.uiEvent.observe(this) {
            when (it) {
                is ShowSaveConfirmDialog -> {
                    AlertDialog.Builder(this)
                        .setTitle("提醒")
                        .setMessage("是否保存当前轨迹？")
                        .setPositiveButton("保存") { _, _ -> viewModel.saveTrace() }
                        .setNegativeButton("删除") { _, _ -> viewModel.abandonTrace() }
                        .show()
                }
            }
        }

        SimpleUiStateObserver.setRequestObserver(this, this, viewModel.commitVMCompose)
        viewModel.commitVMCompose.successUiState.observe(this) {
            SyncDataService.startPush(this)
            finish()
        }
    }

    private fun toggleLocation(start: Boolean) {
        // TODO: start service 应该放在架构哪一层？
        serviceIntent = Intent(this, TraceLocationService::class.java)
        serviceIntent.putExtra(BundleKey.TOGGLE_LOCATION, start)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    override fun onDestroy() {
        toggleLocation(false)
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}