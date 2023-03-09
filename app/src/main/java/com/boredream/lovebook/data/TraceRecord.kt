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
    var synced: Boolean = false, // 是否需要同步（上传）
    var isDelete: Boolean = false, // 软删除
    var syncTimestamp: Long? = null, // 同步数据的时间
) : Belong2UserEntity() {

    @PrimaryKey(autoGenerate = true) var dbId: Long = 0

    @Ignore
    var traceList: ArrayList<TraceLocation> = arrayListOf()

}