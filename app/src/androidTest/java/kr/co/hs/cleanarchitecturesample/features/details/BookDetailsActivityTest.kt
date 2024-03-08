package kr.co.hs.cleanarchitecturesample.features.details

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.navigation.Navigator
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL
import javax.inject.Inject
import kotlin.time.Duration


@Suppress("NonAsciiCharacters", "TestFunctionName")
@HiltAndroidTest
class BookDetailsActivityTest {

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

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                BookDetailsActivity::class.java
            )
        )
    }

    @Test
    fun Book_상세_화면_테스트() = runTest(timeout = Duration.INFINITE) {

        val bookSummaryEntity = object : BookSummaryEntity {
            override val key: String = "9781484206485"
            override val title: String = ""
            override val subtitle: String = ""
            override val price: String = ""
            override val imageUrl: URL? = null
        }
        val details = getBookDetailsUseCase(bookSummaryEntity, scope = this) {}.await()

        activityScenario.onActivity { details?.run { it.setupUI(this) } }

        Espresso.onView(ViewMatchers.withText("Practical MongoDB"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}