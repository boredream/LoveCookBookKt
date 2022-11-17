package com.boredream.lovebook.ui.trace

import com.amap.api.location.AMapLocation
import com.amap.api.mapcore.util.it
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.repo.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TraceMapViewModel @Inject constructor(
    val repository: LocationRepository
) : BaseViewModel() {

    fun startLocation() {
//        repository.onLocationListener = :: onLocationSuccess
//        repository.onTraceListener = :: onTraceSuccess
        repository.startLocation()

//        binding.btnLocationMe.setOnClickListener {
//            viewModel.repository.myLocation?.let { binding.mapView.moveCamera(it) }
//        }
//        viewModel.repository.onLocationListener = {
//            binding.mapView.drawMyLocation(it)
//        }
    }

    private fun onLocationSuccess(location: AMapLocation) {
        // TODO: convert bind info to map view
    }

    private fun onTraceSuccess(list: LinkedList<AMapLocation>) {

    }

}