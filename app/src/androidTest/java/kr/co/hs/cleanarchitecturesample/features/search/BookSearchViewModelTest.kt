package kr.co.hs.cleanarchitecturesample.features.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.features.details.BookDetailsViewModelTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@HiltAndroidTest
class BookSearchViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this) // hilt rule

    @get:Rule
    var instantTaskExecutorRule =
        InstantTaskExecutorRule() // 테스트시 스레드 동기화 coroutineScope 가 한 쓰레드에서 동작

    @Inject
    lateinit var getNewBooksUseCase: GetNewBooksUseCase

    @Inject
    lateinit var searchUseCase: SearchUseCase

    @Before
    fun init() {
        hiltRule.inject()
        Dispatchers.setMain(newSingleThreadContext(BookDetailsViewModelTest::class.java.simpleName))
    }

    @Test
    fun do_test() = runTest(timeout = Duration.INFINITE) {
        val viewModel = BookSearchViewModel(getNewBooksUseCase, searchUseCase)
        viewModel.search("query")

        assertEquals("query", viewModel.getSearchingQuery())
    }

    @After
    fun finished() {
        Dispatchers.resetMain()
    }
}