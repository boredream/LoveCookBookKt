package com.boredream.lovebook.data

import androidx.room.Entity
import androidx.room.Relation

/**
 * 轨迹记录
 */
@Entity
data class TraceRecord(

    @Relation(
        parentColumn = "id",
        entityColumn = "traceRecordId"
    )
    val traceList: ArrayList<TraceLocation>,

    var name: String,
    var startTime: Long,
    var endTime: Long,
    var distance: Int, // 单位米
    var detail: String? = null,
    var traceListStr: String? = null,
) : Belong2UserEntity()