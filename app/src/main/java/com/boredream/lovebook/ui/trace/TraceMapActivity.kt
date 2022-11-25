package com.boredream.lovebook.ui.trace

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.databinding.ActivityTraceMapBinding
import com.boredream.lovebook.service.TraceLocationService
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.yanzhenjie.permission.AndPermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TraceMapActivity : BaseActivity<TraceMapViewModel, ActivityTraceMapBinding>() {

    private lateinit var serviceIntent: Intent

    override fun getLayoutId() = R.layout.activity_trace_map

    override fun getViewModelClass() = TraceMapViewModel::class.java

    companion object {
        fun start(context: Context) {
            val locationPermissions: ArrayList<String> = ArrayList()
            locationPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            locationPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            AndPermission.with(context)
                .runtime()
                .permission(locationPermissions.toTypedArray())
                .onGranted {
                    val intent = Intent(context, TraceMapActivity::class.java)
                    // intent.putExtra(BundleKey.DATA, theDay)
                    context.startActivity(intent)
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(context, locationPermissions)) {
                        PermissionSettingUtil.showSetting(context, permissions)
                    }
                }
                .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        initObserver()
        startLocation()
    }

    private fun initObserver() {
        viewModel.uiEvent.observe(this) {
            println("map event $it")
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
    }

    private fun startLocation() {
        serviceIntent = Intent(this, TraceLocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(serviceIntent)
        viewModel.stop()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}