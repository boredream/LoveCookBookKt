package com.boredream.lovebook.data.repo.source

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.TraceUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * 轨迹记录数据源 - 业务数据
 */
class TraceRecordDataSource @Inject constructor(
    @ApplicationContext val context: Context,
    val apiService: ApiService
) {

    companion object {
        const val TAG = "TraceDataSource"
    }

    fun saveTraceList(traceList: ArrayList<TraceLocation>) {
        if (CollectionUtils.isEmpty(traceList)) return

        // TODO: 是否需要本地数据库？
        saveTraceListToLocal(traceList)

        // TODO: 同步到云端？suspend
        // apiService.xx
    }

    private fun saveTraceListToLocal(traceList: ArrayList<TraceLocation>) {
        val title = TraceUtils.getTraceListName(traceList)

        val fileName = "${title}.txt"
        val file = File(PathUtils.getInternalAppFilesPath(), fileName)
        val sb = StringBuilder()
        traceList.forEach { sb.append(it).append("\n") }
        FileIOUtils.writeFileFromString(file, sb.toString())
        Log.i(TAG, "saveTraceListToLocal $title")
    }

    fun loadTraceList(traceListTitle: String): ArrayList<TraceLocation> {
        // TODO:
        return ArrayList()
    }

}