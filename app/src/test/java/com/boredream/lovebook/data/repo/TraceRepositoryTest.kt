package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.repo.source.TraceRecordDataSource
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
    private lateinit var dataSource: TraceRecordDataSource

    private lateinit var repo: TraceRecordRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = TraceRecordRepository(dataSource)
    }

    @Test
    fun test() = runTest {

    }


}