package kr.co.hs.cleanarchitecturesample.platform

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.test.TestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@HiltAndroidTest
class ActivityTest {
    /**
     * rules
     */
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this) // hilt rule

    lateinit var activityScenario: ActivityScenario<TestActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(TestActivity::class.java)
    }

    @Test
    fun test_unknown_host() {
        activityScenario.onActivity {
            it.showThrowable(UnknownHostException())
        }

        Espresso.onView(ViewMatchers.withText(R.string.error_message_connect))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_time_out() {
        activityScenario.onActivity {
            it.showThrowable(SocketTimeoutException())
        }

        Espresso.onView(ViewMatchers.withText(R.string.error_message_timeout))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}