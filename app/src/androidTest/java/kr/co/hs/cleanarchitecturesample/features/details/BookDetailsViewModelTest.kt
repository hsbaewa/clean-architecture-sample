package kr.co.hs.cleanarchitecturesample.features.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.extension.LiveDataExt.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.time.Duration

@Suppress("NonAsciiCharacters", "TestFunctionName")
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@HiltAndroidTest
class BookDetailsViewModelTest {

    /**
     * rules
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this) // hilt rule

    @get:Rule
    var instantTaskExecutorRule =
        InstantTaskExecutorRule() // 테스트시 스레드 동기화 coroutineScope 가 한 쓰레드에서 동작

    @Inject
    lateinit var getBookDetailsUseCase: GetBookDetailsUseCase

    @Before
    fun init() {
        hiltRule.inject()
        Dispatchers.setMain(newSingleThreadContext(BookDetailsViewModelTest::class.java.simpleName))
    }

    @Test
    fun Book_상세_요청() = runTest(timeout = Duration.INFINITE) {
        val viewModel = BookDetailsViewModel(getBookDetailsUseCase)
        viewModel.request("9781484206485")
        val bookDetails = viewModel.bookDetails.getOrAwaitValue(time = 10)
        assertNotNull(bookDetails)
        val title = bookDetails.find { it is BookDetailItem.Title } as? BookDetailItem.Title
        assertEquals("Practical MongoDB", title?.value)

        viewModel.request("anything")
        val error = viewModel.lastError.getOrAwaitValue(time = 10)
        assertNotNull(error)
        assertEquals(true, error is NullPointerException)
    }

    @After
    fun finished() {
        Dispatchers.resetMain()
    }
}