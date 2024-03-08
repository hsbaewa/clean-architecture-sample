package kr.co.hs.cleanarchitecturesample.features.search

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.hs.cleanarchitecturesample.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BookSearchActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    lateinit var activityScenario: ActivityScenario<BookSearchActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                BookSearchActivity::class.java
            )
        )
    }

    @Test
    fun Book_검색_화면_테스트() {
        Espresso.onView(ViewMatchers.withText(R.string.do_input_search_keyword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        activityScenario.onActivity {
            it.doSearch()
        }

    }
}