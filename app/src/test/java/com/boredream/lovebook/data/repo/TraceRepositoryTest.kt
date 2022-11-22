package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.repo.source.TraceDataSource
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
class TraceRepositoryTest {

    @MockK
    private lateinit var dataSource: TraceDataSource

    private lateinit var repo: TraceRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = TraceRepository(dataSource)
    }

    @Test
    fun testTrace() = runTest {
        // 回调的mock这样处理
        every {
            dataSource.startTrace(any())
        } answers {
            firstArg<(Trace: TraceTrace) -> Unit>()
                .invoke(getTraceTrace())
        }
        repo.startNewTraceList()
        assertNotNull(repo.myTrace)
    }

    @Test
    fun testTrace_notTracing() = runTest {
        // 回调的mock这样处理
        every {
            dataSource.startTrace(any())
        } answers {
            firstArg<(Trace: TraceTrace) -> Unit>()
                .invoke(getTraceTrace())
        }
        repo.startNewTraceList()
        assertEquals(0, repo.traceList.size)
    }

    @Test
    fun testTrace_success() = runTest {
        every {
            dataSource.startTrace(any())
        } answers {
            firstArg<(Trace: TraceTrace) -> Unit>()
                .invoke(getTraceTrace())
            firstArg<(Trace: TraceTrace) -> Unit>()
                .invoke(getTraceTrace(latExtra = 0.1))
        }
        repo.startNewTraceList()
        repo.startNewTraceList()
        assertEquals(2, repo.traceList.size)
    }

    @Test
    fun testTrace_near() = runTest {
        every {
            dataSource.startTrace(any())
        } answers {
            val startTrace = getTraceTrace()
            firstArg<(Trace: TraceTrace) -> Unit>().invoke(startTrace)

            firstArg<(Trace: TraceTrace) -> Unit>().invoke(
                TraceTrace(
                    startTrace.time + 2000,
                    startTrace.latitude + 0.1,
                    startTrace.longitude
                )
            )

            firstArg<(Trace: TraceTrace) -> Unit>().invoke(
                TraceTrace(
                    startTrace.time + 4000,
                    startTrace.latitude,
                    startTrace.longitude
                )
            )

            firstArg<(Trace: TraceTrace) -> Unit>().invoke(
                TraceTrace(
                    startTrace.time + 6000,
                    startTrace.latitude + 0.000001,
                    startTrace.longitude
                )
            )
        }
        repo.startNewTraceList()
        repo.startNewTraceList()
        assertEquals(2, repo.traceList.size)

        val lastIndex = repo.traceList.lastIndex
        val timeDiff = repo.traceList[lastIndex].time - repo.traceList[lastIndex - 1].time
        assertEquals(6000, timeDiff)

    }


}