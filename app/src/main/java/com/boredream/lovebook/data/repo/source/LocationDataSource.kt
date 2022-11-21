package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.data.TraceLocation

interface LocationDataSource {

    /**
     * 开始定位
     */
    fun startLocation(onSuccess: (location: TraceLocation) -> Unit)

    /**
     * 停止定位
     */
    fun stopLocation()

}