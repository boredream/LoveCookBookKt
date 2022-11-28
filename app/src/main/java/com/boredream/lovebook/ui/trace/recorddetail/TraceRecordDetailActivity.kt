package com.boredream.lovebook.ui.trace.recorddetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTraceRecordDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TraceRecordDetailActivity : BaseActivity<TraceRecordDetailViewModel, ActivityTraceRecordDetailBinding>() {

    private lateinit var data: TraceRecord

    override fun getLayoutId() = R.layout.activity_trace_record_detail

    override fun getViewModelClass() = TraceRecordDetailViewModel::class.java

    companion object {

        fun start(context: Context, data: TraceRecord) {
            val intent = Intent(context, TraceRecordDetailActivity::class.java)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        data = intent.extras?.getSerializable(BundleKey.DATA) as TraceRecord
        viewModel.start(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}