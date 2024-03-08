package kr.co.hs.cleanarchitecturesample.features.search

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.hs.cleanarchitecturesample.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("NonAsciiCharacters", "TestFunctionName")
@HiltAndroidTest
class BookSearchActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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
        activityScenario.onActivity {
            it.doSearch()
        }

        Espresso.onView(ViewMatchers.withHint(R.string.do_input_search_keyword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}