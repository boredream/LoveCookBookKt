package com.boredream.lovebook.data.repo.source

import android.util.Log
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.boredream.lovebook.base.BaseEntity
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.net.ApiService
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject

/**
 * 轨迹记录本地数据源
 */
class LocalTraceRecordDataSource @Inject constructor(
    val apiService: ApiService
) {

    companion object {
        const val TAG = "TraceDataSource"
    }

    private fun getDir(): String {
        return File(PathUtils.getInternalAppFilesPath(), "trace").absolutePath
    }

    fun save(data: TraceRecord): ResponseEntity<Boolean> {
        val title = data.title
        val fileName = "${title}.txt"
        val file = File(getDir(), fileName)
        val success = FileIOUtils.writeFileFromString(file, Gson().toJson(data))
        Log.i(TAG, "saveTraceListToLocal $title")
        return ResponseEntity.success(success)
    }

    fun loadTraceList(): ResponseEntity<List<TraceRecord>> {
        val fileList = FileIOUtils.readFile2List(getDir())
        val recordList = ArrayList<TraceRecord>()
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach {
                try {
                    val json = FileIOUtils.readFile2String(it)
                    val record = Gson().fromJson(json, TraceRecord::class.java)
                    recordList.add(record)
                } catch (e: Exception) {
                    //
                }
            }
        }
        return ResponseEntity.success(recordList)
    }

}