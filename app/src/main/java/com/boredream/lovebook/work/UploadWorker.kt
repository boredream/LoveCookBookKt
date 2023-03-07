package com.boredream.lovebook.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amap.api.mapcore.util.it
import com.boredream.lovebook.db.AppDatabase
import com.boredream.lovebook.net.ApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // 获取数据
        val traceRecordDbId = inputData.getString("traceRecordDbId") ?: return Result.failure()
        // TODO: 是不是每次dao都要try catch？
        val traceRecord = appDatabase.traceRecordDao().loadById(traceRecordDbId)
        Log.i(TAG, "doWork: ${traceRecord.name} , trace list size = ${traceRecord.traceList.size}")

        // 执行上传操作
        val sb = StringBuilder()
        traceRecord.traceList.forEach {
            sb.append("_").append(it.time)
                .append(",").append(it.latitude)
                .append(",").append(it.longitude)
        }
        if (sb.isNotEmpty()) {
            traceRecord.traceListStr = sb.substring(1)
        }
        traceRecord.traceList.clear()
//        apiService.addTraceRecord(traceRecord)

        // 如果上传成功，则返回 Result.success()
        return Result.success()

        // TODO 如果上传失败，则返回 Result.failure()
        // return Result.failure()
    }

    companion object {
        const val TAG = "UploadWorker"
    }

}