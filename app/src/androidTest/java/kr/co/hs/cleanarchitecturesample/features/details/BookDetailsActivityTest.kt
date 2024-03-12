package kr.co.hs.cleanarchitecturesample.features.details

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.TestBookStoreRepository
import kr.co.hs.cleanarchitecturesample.data.di.NetworkModule
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.navigation.Navigator
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton


@Suppress("NonAsciiCharacters", "TestFunctionName")
@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class BookDetailsActivityTest {

    /**
     * rules
     */
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this) // hilt rule

    @NavigatorQualifier
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var getBookDetailsUseCase: GetBookDetailsUseCase

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
    }

    @Test
    fun Book_상세_화면_테스트() {
        ActivityScenario.launch<BookDetailsActivity>(
            navigator.createBookDetailsIntent(ApplicationProvider.getApplicationContext(),
                object : BookSummaryEntity {
                    override val key: String = "0"
                    override val title: String = ""
                    override val subtitle: String = ""
                    override val price: String = ""
                    override val imageUrl: URL? = null
                })
        )
        Espresso.onView(ViewMatchers.withText("제목0"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        try {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view_details))
                .perform(ViewActions.swipeUp(), ViewActions.swipeUp())
        } catch (_: Exception) {
        }

        runBlocking { delay(1000) }

        Espresso.onView(ViewMatchers.withText(R.string.details_item_label_preview))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun preview_정보가_2개_이상인_경우() {
        ActivityScenario.launch<BookDetailsActivity>(
            navigator.createBookDetailsIntent(ApplicationProvider.getApplicationContext(),
                object : BookSummaryEntity {
                    override val key: String = "5"
                    override val title: String = ""
                    override val subtitle: String = ""
                    override val price: String = ""
                    override val imageUrl: URL? = null
                })
        )

        try {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view_details))
                .perform(ViewActions.swipeUp(), ViewActions.swipeUp())
        } catch (_: Exception) {
        }

        runBlocking { delay(1000) }

        Espresso.onView(ViewMatchers.withText("preview label1"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withText("preview label2"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}