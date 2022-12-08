package com.boredream.lovebook.data.repo.source

import android.util.Log
import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileIOUtils.readFile2List
import com.blankj.utilcode.util.FileUtils
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
        Log.i(TAG, "saveTraceListToLocal $title , success = $success")
        return ResponseEntity.success(success)
    }

    fun delete(data: TraceRecord): ResponseEntity<Boolean> {
        val title = data.title
        val fileName = "${title}.txt"
        val file = File(getDir(), fileName)
        file.delete()
        Log.i(TAG, "deleteTraceListToLocal $title")
        return ResponseEntity.success(true)
    }

    fun loadTraceList(): ResponseEntity<List<TraceRecord>> {
        val fileList = FileUtils.listFilesInDir(getDir())
        val recordList = ArrayList<TraceRecord>()
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach {
                try {
                    val json = FileIOUtils.readFile2String(it)
                    val record = Gson().fromJson(json, TraceRecord::class.java)
                    recordList.add(record)
                    // LogUtils.i(TAG, "loadTraceList $it")
                } catch (e: Exception) {
                    //
                }
            }
        }
        return ResponseEntity.success(recordList)
    }

}