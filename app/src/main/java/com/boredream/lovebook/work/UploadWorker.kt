package com.boredream.lovebook.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.boredream.lovebook.db.AppDatabase
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.SyncUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.net.SocketException
import java.net.SocketTimeoutException

// TODO: Worker 和 Repository 的关系？
@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
) : CoroutineWorker(appContext, workerParams) {

    private val traceRecordDao = appDatabase.traceRecordDao()
    private val traceLocationDao = appDatabase.traceLocationDao()

    override suspend fun doWork(): Result {
        // 获取数据
        val traceRecordDbId = inputData.getLong("traceRecordDbId", -1L)
        if(traceRecordDbId < 0) {
            return Result.failure()
        }
        // TODO: 是不是每次dao都要try catch？
        val traceRecord = traceRecordDao.loadByDbId(traceRecordDbId)
        val tracePositionList = traceLocationDao.loadByTraceRecordId(traceRecordDbId)

        // 轨迹点
        val sb = StringBuilder()
        tracePositionList.forEach {
            sb.append("_").append(it.time)
                .append(",").append(it.latitude)
                .append(",").append(it.longitude)
        }
        if (sb.isNotEmpty()) {
            traceRecord.traceListStr = sb.substring(1)
        }

        try {
            // 如果上传成功，则返回 Result.success()
            // TODO: 如何处理更新操作？
            val response = apiService.addTraceRecord(traceRecord)
            if (response.isSuccess()) {
                // 更新本地数据库的id
                response.data?.let {
                    traceRecordDao.update(it)
                    // 本地数据更新成功后，记录全局时间戳
                    SyncUtils.update(it.syncTimestamp)
                    Log.i(TAG, "upload success: ${it.name} , update = ${it.id}")
                }
                return Result.success()
            }
        } catch (e: SocketTimeoutException) {
            // 处理网络连接超时异常
            return Result.retry()
        } catch (e: SocketException) {
            // 处理网络异常（如断网）
            return Result.retry()
        }

        return Result.failure()
    }

    companion object {
        const val TAG = "UploadWorker"
    }

}