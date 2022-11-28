package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants.getStepTraceLocation
import com.boredream.lovebook.TestDataConstants.getTraceLocation
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.LocationDataSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocationRepositoryTest {

    @MockK
    private lateinit var dataSource: LocationDataSource

    private lateinit var repo: LocationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = LocationRepository(dataSource)
    }

    @Test
    fun testLocation() = runTest {
        // 回调的mock这样处理
        every {
            dataSource.startLocation(any())
        } answers {
            firstArg<(location: TraceLocation) -> Unit>()
                .invoke(getTraceLocation())
        }
        repo.startLocation()
        assertNotNull(repo.myLocation)
    }

    @Test
    fun testTrace_notTracing() = runTest {
        // 回调的mock这样处理
        every {
            dataSource.startLocation(any())
        } answers {
            firstArg<(location: TraceLocation) -> Unit>()
                .invoke(getTraceLocation())
        }
        repo.startLocation()
        assertEquals(0, repo.traceList.size)
    }

    @Test
    fun testTrace_success() = runTest {
        every {
            dataSource.startLocation(any())
        } answers {
            firstArg<(location: TraceLocation) -> Unit>()
                .invoke(getStepTraceLocation())
            firstArg<(location: TraceLocation) -> Unit>()
                .invoke(getStepTraceLocation())
        }
        repo.startTrace()
        repo.startLocation()
        assertEquals(2, repo.traceList.size)
    }

    @Test
    fun testTrace_stepTooNear() = runTest {
        every {
            dataSource.startLocation(any())
        } answers {
            val startLocation = getTraceLocation()
            firstArg<(location: TraceLocation) -> Unit>().invoke(startLocation)

            firstArg<(location: TraceLocation) -> Unit>().invoke(
                TraceLocation(
                    startLocation.time + 2000,
                    startLocation.latitude + 0.1,
                    startLocation.longitude
                )
            )

            firstArg<(location: TraceLocation) -> Unit>().invoke(
                TraceLocation(
                    startLocation.time + 4000,
                    startLocation.latitude,
                    startLocation.longitude
                )
            )

            firstArg<(location: TraceLocation) -> Unit>().invoke(
                TraceLocation(
                    startLocation.time + 6000,
                    startLocation.latitude + 0.000001,
                    startLocation.longitude
                )
            )
        }
        repo.startTrace()
        repo.startLocation()
        assertEquals(2, repo.traceList.size)

        val lastIndex = repo.traceList.lastIndex
        val timeDiff = repo.traceList[lastIndex].time - repo.traceList[lastIndex - 1].time
        assertEquals(6000, timeDiff)
    }

    @Test
    fun testTrace_stepTooFar() = runTest {
        repo.startTrace()
        // 过滤器需要前3个定位点作为参考，不作为轨迹记录
        for(i in 0..10) {
            repo.onLocationSuccess(getStepTraceLocation())
            println(repo.traceList.size)
        }
//        repo.onLocationSuccess(getStepTraceLocation())
//        repo.onLocationSuccess(getStepTraceLocation())
//
//        repo.onLocationSuccess(getStepTraceLocation())
//        assertEquals(2, repo.traceList.size)

        // 距离过远，不计入
//        val trace = getStepTraceLocation()
//        trace.latitude += 0.1
//        repo.onLocationSuccess(trace)
//        assertEquals(2, repo.traceList.size)
    }

    @Test
    fun testTrace_clear() = runTest {
        // 因为 dataSource 定位是定时持续返回的，而非每次调用 startLocation 返回
        // 所以多段多次回调，不用 answers arg 方式，直接手动 onSuccess 触发回调
        repo.startTrace()
        repo.onLocationSuccess(getStepTraceLocation())
        repo.onLocationSuccess(getStepTraceLocation())

        assertEquals(2, repo.traceList.size)

        // 停止记录轨迹
        repo.stopTrace()
        repo.onLocationSuccess(getStepTraceLocation())
        assertEquals(2, repo.traceList.size)

        // 清理轨迹
        repo.clearTraceList()
        assertEquals(0, repo.traceList.size)

        // 再次开启轨迹记录
        repo.startTrace()
        repo.onLocationSuccess(getStepTraceLocation())
        repo.onLocationSuccess(getStepTraceLocation())
        repo.onLocationSuccess(getStepTraceLocation())
        assertEquals(3, repo.traceList.size)

    }

}