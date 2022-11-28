package com.boredream.lovebook.ui.trace

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.boredream.lovebook.ui.trace.editmap.TraceEditMapActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class TraceEditMapActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityTestRule(TraceEditMapActivity::class.java, true, false)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testAll() {
        activityRule.launchActivity(Intent())
        sleep(60 * 60 * 1000)
    }

}
