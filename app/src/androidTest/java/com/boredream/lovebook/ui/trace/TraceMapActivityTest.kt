package com.boredream.lovebook.ui.trace

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.amap.api.location.AMapLocation
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.source.GdLocationDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class TraceMapActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityTestRule(TraceMapActivity::class.java, true, false)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testAll() {
        // latitude=31.217792#longitude=121.355379#province=上海市#coordType=GCJ02#city=上海市#district=长宁区#cityCode=021#adCode=310105#address=上海市长宁区协和路789号靠近山里亲戚(淞沪路店)#country=中国#road=协和路#poiName=山里亲戚(淞沪路店)#street=协和路#streetNum=789号#aoiName=中山国际广场#poiid=B0FFHRUNOP#floor=#errorCode=0#errorInfo=success#locationDetail=#id:SbDYzNzVjOTM5ZjAwNW9tb2ZqcGlqNjRhZjhiN2Y3LA==#csid:8297593f13e64158828cf8685b294b01#description=在山里亲戚(淞沪路店)附近#locationType=5#conScenario=99
        val startLocation = AMapLocation("test")
        startLocation.latitude = 31.227792
        startLocation.longitude = 121.355379

        val locationDataSource = mockk<GdLocationDataSource>()
        val repo = LocationRepository(locationDataSource)
        repo.myLocation = AMapLocation("test")

        activityRule.launchActivity(null)

        sleep(10 * 60 * 1000)
    }

}
