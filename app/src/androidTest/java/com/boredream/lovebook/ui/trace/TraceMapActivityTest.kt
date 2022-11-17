package com.boredream.lovebook.ui.trace

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.amap.api.location.AMapLocation
import com.boredream.lovebook.data.repo.DefaultLocationRepository
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
        activityRule.launchActivity(null)
        sleep(10 * 60 * 1000)
    }

}
