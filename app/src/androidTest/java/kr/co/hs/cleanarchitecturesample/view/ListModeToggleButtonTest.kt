package kr.co.hs.cleanarchitecturesample.view

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kr.co.hs.cleanarchitecturesample.test.TestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class ListModeToggleButtonTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var activityScenario: ActivityScenario<TestActivity>

    @Before
    fun init() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(TestActivity::class.java)
    }

    @Test
    fun do_test() {
        var viewId = 0
        var toggleListener: ToggleListener? = null
        activityScenario.onActivity {
            val viewGroup = it.window.decorView.findViewById<ViewGroup>(android.R.id.content)

            viewId = ViewCompat.generateViewId()
            val button = ListModeToggleButton(it)
                .apply { id = viewId }

            toggleListener = ToggleListener(button.listMode)
            button.setOnToggleListener(toggleListener)

            viewGroup.addView(
                button,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            assertEquals(ListModeToggleButton.MODE_LINEAR, button.listMode)
            assertEquals(ListModeToggleButton.MODE_LINEAR, toggleListener?.listMode)
        }

        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())

        activityScenario.onActivity {
            val button = it.findViewById<ListModeToggleButton>(viewId)
            assertEquals(ListModeToggleButton.MODE_GRID, button.listMode)
            assertEquals(ListModeToggleButton.MODE_GRID, toggleListener?.listMode)
        }

        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
        activityScenario.onActivity {
            val button = it.findViewById<ListModeToggleButton>(viewId)
            assertEquals(ListModeToggleButton.MODE_LINEAR, button.listMode)
            assertEquals(ListModeToggleButton.MODE_LINEAR, toggleListener?.listMode)
        }
    }

    private class ToggleListener(@ListModeToggleButton.ListMode currentListMode: Int) :
        ListModeToggleButton.OnToggleListener {
        @ListModeToggleButton.ListMode
        var listMode = currentListMode
        override fun onChangedMode(@ListModeToggleButton.ListMode listMode: Int) {
            this.listMode = listMode
        }

    }
}