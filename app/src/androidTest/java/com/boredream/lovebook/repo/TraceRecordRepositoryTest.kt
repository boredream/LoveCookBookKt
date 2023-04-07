package com.boredream.lovebook.repo

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amap.api.mapcore.util.it
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.PrintLogger
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.data.repo.source.ConfigLocalDataSource
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.data.repo.source.TraceRecordRemoteDataSource
import com.boredream.lovebook.db.AppDatabase
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import com.boredream.lovebook.utils.Logger
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TraceRecordRepositoryTest {

    private val logger: Logger = Logger()
    private lateinit var remoteDataSource: TraceRecordRemoteDataSource
    private lateinit var repo: TraceRecordRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "trace-db").build()

        ServiceCreator.tokenFactory =
            { "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiZXhwIjoxNjgzMTk0Mjk3fQ.Yev5bAw3v_rMnbCF8BnKXjJGitPKcxzF8H9LyQwTllk" }
        val apiService = ServiceCreator.create(ApiService::class.java)
        remoteDataSource = TraceRecordRemoteDataSource(apiService)

        // 测试真实数据
        repo = TraceRecordRepository(
            logger,
            ConfigLocalDataSource(),
            remoteDataSource,
            TraceRecordLocalDataSource(db)
        )
    }

    @Test
    fun testQuery() = runTest {
        val recordId = "46"
        val traceList = remoteDataSource.getTraceLocationList(recordId).getSuccessData()
        var lastPosition = traceList[0]
        logger.i("INVALID POSITION check start, size = ${traceList.size}")
        for (index in 1 until traceList.size) {
            val curPosition = traceList[index]
            val distance = AMapUtils.calculateLineDistance(
                LatLng(lastPosition.latitude, lastPosition.longitude),
                LatLng(curPosition.latitude, curPosition.longitude)
            )
            val maxDistance: Double = 0.02 * (curPosition.time - lastPosition.time)
//            if (distance < 0.5) {
//                logger.i("INVALID POSITION near = $index : $curPosition")
//            } else
            if (distance > maxDistance) {
                // 如果超过最大距离了
                logger.i("INVALID POSITION max = ${curPosition.id} : $curPosition")
            } else {
                // 未超过最大距离，才作为可信坐标参考
                lastPosition = curPosition
            }
        }
    }

}