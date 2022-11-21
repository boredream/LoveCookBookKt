package com.boredream.lovebook.ui.trace

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.databinding.ActivityTraceMapBinding
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.yanzhenjie.permission.AndPermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TraceMapActivity : BaseActivity<TraceMapViewModel, ActivityTraceMapBinding>() {

    override fun getLayoutId() = R.layout.activity_trace_map

    override fun getViewModelClass() = TraceMapViewModel::class.java

    companion object {
        fun start(context: Context) {
            val locationPermissions: ArrayList<String> = ArrayList()
            locationPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            locationPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                locationPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

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

//        intent.extras?.let {
//            theDay = it.getSerializable(BundleKey.DATA) as TheDay?
//        }
        binding.mapView.onCreate(savedInstanceState)

        // FIXME: 4 test
//        binding.btnTestStep.setOnClickListener {
//            (viewModel.repository as FakeLocationRepository).testStepLocation()
//        }

        viewModel.mapEvent.observe(this) {
            when(it) {
                is DrawMyLocation -> binding.mapView.drawMyLocation(it.location)
                is MoveToLocation -> binding.mapView.moveCamera(it.location)
                is DrawTraceLine -> binding.mapView.drawTraceList(it.locationList)
            }
        }
        viewModel.startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行binding.mapView.onDestroy()，销毁地图
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行binding.mapView.onResume ()，重新绘制加载地图
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行binding.mapView.onPause ()，暂停地图的绘制
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行binding.mapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.mapView.onSaveInstanceState(outState)
    }

}