package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TraceUseCaseTest {

    @MockK
    private lateinit var traceRecordRepository: TraceRecordRepository

    @MockK
    private lateinit var locationRepository: LocationRepository

    private lateinit var useCase: TraceUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = TraceUseCase(locationRepository, traceRecordRepository)

        // use case 是连接多个 repo 的逻辑处理类，大部分时候只需要测试 repo 的 xx 函数是否调用
        // 因为 repo 是 mockk 的，所以无法获取 repo 里持有的数据是否正确
        every { locationRepository.startLocation() } just runs
        every { locationRepository.clearTraceList() } just runs
        every { locationRepository.startTrace() } just runs
    }


    @Test
    fun test() = runTest {
        useCase.startLocation()
        verify(exactly = 1) { locationRepository.startLocation() }

        useCase.startTrace()
        verify(exactly = 1) { locationRepository.clearTraceList() }
        verify(exactly = 1) { locationRepository.startTrace() }
    }

}