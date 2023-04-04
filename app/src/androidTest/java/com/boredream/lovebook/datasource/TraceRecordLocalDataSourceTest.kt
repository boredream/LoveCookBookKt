package com.boredream.lovebook.datasource

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.db.AppDatabase
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TraceRecordLocalDataSourceTest {

    private lateinit var dataSource: TraceRecordLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dataSource = TraceRecordLocalDataSource(db)
    }

    @Test
    fun testAddAndDelete() = runTest {
        val nearRecord = TraceRecord("近", 0, 0, 0)
        nearRecord.traceList = arrayListOf(
            TraceLocation(120.0001, 30.0),
            TraceLocation(120.0002, 30.0),
            TraceLocation(120.0003, 30.0),
            TraceLocation(120.0004, 30.0),
            TraceLocation(120.0005, 30.0),
        )
        val addResponse = dataSource.add(nearRecord)

        nearRecord.dbId = addResponse.data!!.dbId
        var getResponse = dataSource.getTraceRecordByDbId(nearRecord.dbId)
        Assert.assertEquals(getResponse.getSuccessData().name, nearRecord.name)

        var listResponse = dataSource.getTraceLocationList(nearRecord.dbId)
        Assert.assertEquals(listResponse.getSuccessData().size, 5)

        // 软删除
        dataSource.delete(nearRecord)
        getResponse = dataSource.getTraceRecordByDbId(nearRecord.dbId)
        Assert.assertTrue(getResponse.getSuccessData().isDelete)

        listResponse = dataSource.getTraceLocationList(nearRecord.dbId)
        Assert.assertEquals(listResponse.getSuccessData().size, 0)
    }

    @Test
    fun testGetNearbyList() = runTest {
        val targetLat = 120.0
        val targetLng = 30.0

        // near 120.0001 ~ 120.0005
        val nearRecord = TraceRecord("近", 0, 0, 0)
        val nearLocationList = mutableListOf<TraceLocation>()
        for (i in 1..5) {
            nearLocationList.add(TraceLocation(targetLat + i * 0.0001, targetLng))
        }
        nearRecord.traceList = ArrayList(nearLocationList)
        dataSource.add(nearRecord)

        // near 121.0001 ~ 121.0005
        val farRecord = TraceRecord("远", 0, 0, 0)
        val farLocationList = mutableListOf<TraceLocation>()
        for (i in 1..5) {
            farLocationList.add(TraceLocation(targetLat + 1 + i * 0.0001, targetLng))
        }
        farRecord.traceList = ArrayList(farLocationList)
        dataSource.add(farRecord)

        // assert
        Assert.assertEquals(2, dataSource.getList().getSuccessData().size)

        // find 120 +- 0.001
        val range = 0.001
        val response = dataSource.getNearbyList(targetLat, targetLng, range)
        Assert.assertEquals(1, response.getSuccessData().size)
        Assert.assertEquals("近", response.getSuccessData()[0].name)
    }
}