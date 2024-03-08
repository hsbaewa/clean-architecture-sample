package kr.co.hs.cleanarchitecturesample.features.details

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration


@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BookDetailsActivityTest {

    /**
     * rules
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this) // hilt rule

    lateinit var activityScenario: ActivityScenario<BookDetailsActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                BookDetailsActivity::class.java
            ).apply { putExtra("key", "9781484206485") }
        )
    }

    @Test
    fun Book_상세_화면_테스트() = runTest(timeout = Duration.INFINITE) {
        activityScenario.onActivity {
            val key = it.intent.getStringExtra("key")
            assertNotNull(key)

            it.doRequestBookDetails()
        }

        Espresso.onView(ViewMatchers.withText("Practical MongoDB"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}