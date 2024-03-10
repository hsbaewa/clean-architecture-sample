package kr.co.hs.cleanarchitecturesample.view

import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import kr.co.hs.cleanarchitecturesample.test.TestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL
import javax.inject.Inject
import kotlin.time.Duration

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BookInfoLinearItemViewTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BookStoreRepositoryQualifier
    @Inject
    lateinit var bookStoreRepository: BookStoreRepository

    lateinit var activityScenario: ActivityScenario<TestActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(TestActivity::class.java)
    }

    @Test
    fun do_Test() = runTest(timeout = Duration.INFINITE) {
        activityScenario.onActivity {
            val viewGroup = it.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            viewGroup.addView(BookInfoLinearItemView(it))
        }

        Espresso.onView(ViewMatchers.withId(R.id.tv_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.tv_sub_title))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))

        activityScenario.onActivity {
            val viewGroup = it.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            viewGroup.removeAllViews()
            val bookSummaryEntity = object : BookSummaryEntity {
                override val key: String = ""
                override val title: String = "title"
                override val subtitle: String = "subtitle"
                override val price: String = "0원"
                override val imageUrl: URL = URL("https://picsum.photos/200/300")
            }
            viewGroup.addView(BookInfoLinearItemView(it).apply { set(bookSummaryEntity) })
        }

        Espresso.onView(ViewMatchers.withText("title"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("subtitle"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("0원"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val entity = withContext(Dispatchers.IO) {
            bookStoreRepository.getNewBooks().first()
        }

        activityScenario.onActivity {
            val viewGroup = it.window.decorView.findViewById<ViewGroup>(android.R.id.content)
            viewGroup.removeAllViews()
            viewGroup.addView(BookInfoLinearItemView(it).apply { set(entity) })
        }

        Espresso.onView(ViewMatchers.withId(R.id.tv_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(entity.title)))
        Espresso.onView(ViewMatchers.withId(R.id.tv_sub_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(entity.subtitle)))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price))
            .check(ViewAssertions.matches(ViewMatchers.withText(entity.price)))
    }
}