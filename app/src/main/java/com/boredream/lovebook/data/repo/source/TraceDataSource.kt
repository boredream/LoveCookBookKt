package com.boredream.lovebook.data.repo.source

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.TraceUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/**
 * 轨迹数据源-应用业务相关信息
 */
class TraceDataSource @Inject constructor(
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
        FileIOUtils.writeFileFromString(file, Gson().toJson(traceList))
        Log.i(TAG, "saveTraceListToLocal $title")
    }

    fun loadTraceList(traceListTitle: String): ArrayList<TraceLocation> {
        // TODO:  
        return loadTraceListFromLocal(traceListTitle)
    }

    private fun loadTraceListFromLocal(traceListTitle: String): ArrayList<TraceLocation> {
        val fileName = "${traceListTitle}.txt"
        val file = File(PathUtils.getInternalAppFilesPath(), fileName)
        val json = FileIOUtils.readFile2String(file)
        Log.i(TAG, "loadTraceListFromLocal $traceListTitle")
        return Gson().fromJson(json, object : TypeToken<ArrayList<TraceLocation>>(){}.type)
    }

}