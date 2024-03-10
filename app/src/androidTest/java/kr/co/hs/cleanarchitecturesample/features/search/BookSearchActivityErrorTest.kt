package kr.co.hs.cleanarchitecturesample.features.search

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.data.di.NetworkModule
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class BookSearchActivityErrorTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var activityScenario: ActivityScenario<BookSearchActivity>

    @Module
    @InstallIn(SingletonComponent::class)
    object TestNetworkModule {
        @Singleton
        @Provides
        @BookStoreRepositoryQualifier
        fun providerBookStoreRepository(): BookStoreRepository = object : BookStoreRepository {
            override fun search(query: String): Flow<BookSummaryEntity> {
                throw Exception("error")
            }

            override fun search(query: String, page: Int): Flow<BookSummaryEntity> {
                throw Exception("error")
            }

            override fun getNewBooks(): Flow<BookSummaryEntity> {
                throw Exception("error")
            }

            override fun getBookDetails(bookSummaryEntity: BookSummaryEntity): Flow<BookDetailEntity> {
                throw Exception("error")
            }

        }
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
    fun do_test() {
        Espresso.onView(ViewMatchers.withText("error"))
            .inRoot(isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("error"))
            .inRoot(isDialog())
            .perform(ViewActions.pressBack())

        Espresso.onView(ViewMatchers.withHint(R.string.do_input_search_keyword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.edit_query))
            .perform(ViewActions.typeText("subject"))

        Espresso.onView(ViewMatchers.withId(R.id.edit_query))
            .perform(ViewActions.pressImeActionButton())

        Espresso.onView(ViewMatchers.withText("error"))
            .inRoot(isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}