package kr.co.hs.cleanarchitecturesample.features.details

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.data.di.NetworkModule
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
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
import kotlin.time.Duration


@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class BookDetailsActivityErrorTest {

    /**
     * rules
     */
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this) // hilt rule

    lateinit var activityScenario: ActivityScenario<BookDetailsActivity>

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
        fun providerBookStoreRepository(): BookStoreRepository = object : BookStoreRepository {
            override fun search(query: String): Flow<BookSummaryEntity> {
                throw Exception()
            }

            override fun search(query: String, page: Int): Flow<BookSummaryEntity> {
                throw Exception()
            }

            override fun getNewBooks(): Flow<BookSummaryEntity> {
                throw Exception()
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
            navigator.createBookDetailsIntent(ApplicationProvider.getApplicationContext(),
                object : BookSummaryEntity {
                    override val key: String = "0"
                    override val title: String = ""
                    override val subtitle: String = ""
                    override val price: String = ""
                    override val imageUrl: URL? = null
                })
        )
    }

    @Test
    fun do_test() = runTest(timeout = Duration.INFINITE) {
        Espresso.onView(ViewMatchers.withText("error"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}