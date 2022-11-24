package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.repo.source.LocalTraceRecordDataSource
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TraceRepositoryTest {

    @MockK
    private lateinit var dataSource: LocalTraceRecordDataSource

    private lateinit var repo: TraceRecordRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test() = runTest {

    }


}