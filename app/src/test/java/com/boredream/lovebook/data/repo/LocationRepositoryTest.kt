package com.boredream.lovebook.data.repo

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
                .invoke(getTraceLocation())
            firstArg<(location: TraceLocation) -> Unit>()
                .invoke(getTraceLocation(latExtra = 0.1))
        }
        repo.startTrace()
        repo.startLocation()
        assertEquals(2, repo.traceList.size)
    }

    @Test
    fun testTrace_near() = runTest {
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


}