package kr.co.hs.cleanarchitecturesample.features.search

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.TestBookStoreRepository
import kr.co.hs.cleanarchitecturesample.data.di.NetworkModule
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@Suppress("NonAsciiCharacters", "TestFunctionName")
@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class BookSearchActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var activityScenario: ActivityScenario<BookSearchActivity>

    @Module
    @InstallIn(SingletonComponent::class)
    object TestNetworkModule {
        @Singleton
        @Provides
        @BookStoreRepositoryQualifier
        fun providerBookStoreRepository(): BookStoreRepository = TestBookStoreRepository()
    }

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
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        Espresso.onView(ViewMatchers.withHint(R.string.do_input_search_keyword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.edit_query))
            .perform(ViewActions.typeText("subject"))

        Espresso.onView(ViewMatchers.withId(R.id.edit_query))
            .perform(ViewActions.pressImeActionButton())

        Espresso.onView(ViewMatchers.withId(R.id.toggle_list_mode))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("subject3"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("subject3"))
            .perform(ViewActions.click())

        assertNotEquals(Lifecycle.State.RESUMED, activityScenario.state)
    }
}