package com.boredream.lovebook.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.data.repo.TraceRecordRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


/**
 * 同步数据服务
 */
@AndroidEntryPoint
class SyncDataService : Service() {

    // service 和 work manager 区别？
    // 共性是都可以作为「后台任务」
    //
    // 区别是 work manager 是为了解决多次应用生命周期中，持久化处理任务的需求。
    // 因此任务是保存在sqlite里的，且提供复杂的规则，可以线性、并行、重试、根据场景（wifi环境）工作等。
    //
    // server更简单些，后台任务
    //
    // 这里同步数据的任务因为本身轨迹就是记录在db里的，且同步状态记录在数据信息里，所以不用work manager也行

    companion object {
        const val SERVICE_ID = 110119121
        const val CHANNEL_ID = "com.boredream.lovebook.service.syncdata"

        const val BUNDLE_KEY_ACTION = "action"
        const val ACTION_SYNC = "sync"
        const val ACTION_PUSH = "push"
        const val ACTION_ADD = "add"

        fun startSync(context: Context) {
            val intent = Intent(context, SyncDataService::class.java)
            intent.putExtra(BUNDLE_KEY_ACTION, ACTION_SYNC)
            context.startService(intent)
        }

        fun startPush(context: Context) {
            val intent = Intent(context, SyncDataService::class.java)
            intent.putExtra(BUNDLE_KEY_ACTION, ACTION_PUSH)
            context.startService(intent)
        }

        fun startAddTraceRecord(context: Context, traceRecord: TraceRecord) {
            val intent = Intent(context, SyncDataService::class.java)
            intent.putExtra(BUNDLE_KEY_ACTION, ACTION_ADD)
            intent.putExtra(BundleKey.ID, traceRecord.dbId)
            context.startService(intent)
        }
    }

    @Inject
    lateinit var traceRecordRepository: TraceRecordRepository

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        LogUtils.i("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            it.getStringExtra(BUNDLE_KEY_ACTION)?.let { action ->
                LogUtils.i("action = $action")
                when (action) {
                    ACTION_SYNC -> scope.launch { traceRecordRepository.syncData() }
                    ACTION_PUSH -> scope.launch { traceRecordRepository.syncDataPush() }
                    ACTION_ADD -> {
                        val dbId = it.getLongExtra(BundleKey.ID, -1)
                        scope.launch { traceRecordRepository.add2remote(dbId) }
                    }
                    else -> {}
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}