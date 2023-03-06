package com.boredream.lovebook.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 轨迹记录
 */
@Entity
data class TraceRecord(
    var name: String,
    var startTime: Long,
    var endTime: Long,
    var distance: Int, // 单位米
    var detail: String? = null,
    var traceListStr: String? = null,
) : Belong2UserEntity() {

    @PrimaryKey(autoGenerate = true) var dbId: Long = 0

    @Ignore
    var traceList: ArrayList<TraceLocation> = arrayListOf()

}