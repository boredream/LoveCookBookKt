package com.boredream.lovebook.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord

/**
 * Room建议的一对多方案，使用中间表
 * https://developer.android.com/training/data-storage/room/relationships?hl=zh-cn#one-to-many
 *
 * TODO 也可以不用中间表，直接在一对多的「一」对象里添加 @Relation
 */
data class TraceRecordWithLocation(

    // one
    @Embedded val traceRecord: TraceRecord,

    // many
    @Relation(
        parentColumn = "id", // 父表的主键
        entityColumn = "traceRecordId" // 子表中关联父表主键的id
    )
    val traceLocationList: List<TraceLocation>

)