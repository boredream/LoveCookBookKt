package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.data.repo.source.TraceRecordRemoteDataSource
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.MockUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify

@OptIn(ExperimentalCoroutinesApi::class)
class TraceRecordRepositoryTest {

    @MockK
    private lateinit var service: ApiService

    @MockK
    private lateinit var remoteDataSource: TraceRecordRemoteDataSource

    @MockK
    private lateinit var localDataSource: TraceRecordLocalDataSource

    private lateinit var repo: TraceRecordRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repo = TraceRecordRepository(service, remoteDataSource, localDataSource)
    }

    @Test
    fun testSyncDataPullSuccess() = runTest {
        // 模拟 service 的响应
        val record = TraceRecord("", 0L, 0L, 0)
        val responseEntity = ResponseEntity.success(arrayListOf(record))
        every { runBlocking { service.getTraceRecordListBySyncTimestamp(any()) } } returns responseEntity

        // 模拟 local data source 的响应
        every { runBlocking { localDataSource.getTraceRecordByDbId(any()) } } returns ResponseEntity.success(record)
        every { runBlocking { localDataSource.getTraceLocationList(any()) } } returns ResponseEntity.success(arrayListOf())
        every { runBlocking { localDataSource.add(any()) } } returns ResponseEntity.success(record)

        // 模拟 remote data source 的响应
        every { runBlocking { remoteDataSource.getTraceLocationList(any()) } } returns ResponseEntity.success(arrayListOf())

        // 调用被测试的方法
        val repo = TraceRecordRepository(service, remoteDataSource, localDataSource)
        repo.syncDataPull()

        // 验证对 local data source 和 remote data source 的调用
        verify { runBlocking { localDataSource.getTraceRecordByDbId(any()) } }
        verify { runBlocking { localDataSource.getTraceLocationList(any()) } }
        verify { runBlocking { localDataSource.add(any()) } }
        verify { runBlocking { remoteDataSource.getTraceLocationList(any()) } }

        // 验证对 service 的调用
        verify(exactly = 0) { runBlocking { service.getTraceRecordListBySyncTimestamp(any()) } }
    }

}