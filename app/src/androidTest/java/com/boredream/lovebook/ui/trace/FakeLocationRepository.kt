package com.boredream.lovebook.ui.trace

import com.amap.api.location.AMapLocation
import com.boredream.lovebook.data.repo.LocationRepository
import javax.inject.Inject

class FakeLocationRepository @Inject constructor() : LocationRepository() {

    override fun startLocation() {
        println("FakeLocationRepository startLocation")
    }

    override fun appendTracePoint(location: AMapLocation) {
        println("FakeLocationRepository appendTracePoint")
    }

    override fun call(t: AMapLocation) {
        println("FakeLocationRepository call")
    }

}