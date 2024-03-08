package kr.co.hs.cleanarchitecturesample.navigation

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.test.TestActivity
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL
import javax.inject.Inject

@Suppress("NonAsciiCharacters", "TestFunctionName")
@HiltAndroidTest
class NavigatorTest {
    /**
     * rules
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this) // hilt rule

    lateinit var activityScenario: ActivityScenario<TestActivity>

    @NavigatorQualifier
    @Inject
    lateinit var navigator: Navigator

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(TestActivity::class.java)
    }

    @Test
    fun 검색_화면_실행() {
        activityScenario.onActivity {
            navigator.startSearch(it)
        }

        assertNotEquals(Lifecycle.State.RESUMED, activityScenario.state)
    }

    @Test
    fun 상세_화면_실행() {
        activityScenario.onActivity {
            val summary = object : BookSummaryEntity {
                override val key: String = ""
                override val title: String = ""
                override val subtitle: String = ""
                override val price: String = ""
                override val imageUrl: URL? = null
            }
            navigator.startDetail(it, summary)
        }

        assertNotEquals(Lifecycle.State.RESUMED, activityScenario.state)
    }
}