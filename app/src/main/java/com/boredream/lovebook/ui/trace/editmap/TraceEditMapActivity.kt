package com.boredream.lovebook.ui.trace.editmap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.FileIOUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.databinding.ActivityTraceEditMapBinding
import com.boredream.lovebook.utils.TraceFilter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TraceEditMapActivity : BaseActivity<TraceEditMapViewModel, ActivityTraceEditMapBinding>() {

    private lateinit var data: TraceRecord

    override fun getLayoutId() = R.layout.activity_trace_edit_map

    override fun getViewModelClass() = TraceEditMapViewModel::class.java

    companion object {

        fun start(context: Context, data: TraceRecord) {
            val intent = Intent(context, TraceEditMapActivity::class.java)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var repo : TraceRecordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        // data = intent.extras?.getSerializable(BundleKey.DATA) as TraceRecord
        // FIXME: 4 test
        val json = FileIOUtils.readFile2String(
            "/data/data/com.boredream.lovebook/files/trace/轨迹 2022-11-27 15:10:09.txt")
        val oldData = Gson().fromJson(json, TraceRecord::class.java)
        val filter = TraceFilter()
        data = TraceRecord(
            ArrayList(oldData.traceList.filter { filter.filterPos(it) }),
            "test", 0, 0, 100
        )

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