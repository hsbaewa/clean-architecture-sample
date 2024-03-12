package kr.co.hs.cleanarchitecturesample

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL
import javax.inject.Inject
import kotlin.time.Duration

@Suppress("TestFunctionName", "NonAsciiCharacters")
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DataTest {
    @Inject
    lateinit var getBookDetailsUseCase: GetBookDetailsUseCase

    @Inject
    lateinit var getNewBooksUseCase: GetNewBooksUseCase

    @Inject
    lateinit var searchUseCase: SearchUseCase

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() = hiltRule.inject()

    @Test
    fun Book_검색_테스트() = runTest(timeout = Duration.INFINITE) {
        coroutineScope {
            searchUseCase("mongodb", scope = this) {
                when (it) {
                    is UseCaseResult.Error -> when (it.e) {
                        SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                    }

                    is UseCaseResult.Exception -> throw it.t
                    is UseCaseResult.Success -> {}
                }
            }.await().let {
                assertTrue(it.isNotEmpty())
            }
        }

        coroutineScope {
            var isLoadAll = false
            var page = 1
            val list = ArrayList<BookSummaryEntity>()
            while (!isLoadAll) {
                searchUseCase("mongodb", page = page, scope = this) {
                    when (it) {
                        is UseCaseResult.Error -> when (it.e) {
                            SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                        }

                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {}
                    }
                }.await().let {
                    if (it.isEmpty()) {
                        isLoadAll = true
                    }
                    page++
                    list.addAll(it)
                }
            }

            assertEquals(80, list.size)
        }

        coroutineScope {
            searchUseCase("아무거나", scope = this) {
                when (it) {
                    is UseCaseResult.Error -> when (it.e) {
                        SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                    }

                    is UseCaseResult.Exception -> throw it.t
                    is UseCaseResult.Success -> {}
                }
            }.await().let {
                assertTrue(it.isEmpty())
            }
        }


        assertThrows(Exception::class.java) {
            runBlocking {
                @Suppress("DeferredResultUnused")
                searchUseCase("", scope = this) {
                    when (it) {
                        is UseCaseResult.Error -> when (it.e) {
                            SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                        }

                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {}
                    }
                }
            }
        }
    }

    @Test
    fun Book_New_Release_조회_테스트() = runTest {
        val result = getNewBooksUseCase(
            scope = this,
            onResult = {
                if (it is UseCaseResult.Exception) {
                    throw it.t
                }
            }
        ).await()
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun Book_상세_조회_테스트() = runTest(timeout = Duration.INFINITE) {
        val result = coroutineScope {
            // 유효하지 않은 isbn
            getBookDetailsUseCase(
                object : BookSummaryEntity {
                    override val key: String = "anything"
                    override val title: String = ""
                    override val subtitle: String = ""
                    override val price: String = ""
                    override val imageUrl: URL? = null
                },
                scope = this,
                onResult = {
                    when (it) {
                        is UseCaseResult.Error -> when (it.e) {
                            GetBookDetailsUseCase.NotFoundBookDetails -> {}
                        }

                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {}
                    }
                }
            )
        }.await()

        assertNull(result)

        // 실제 유효한 isbn
        val detailsList = coroutineScope {
            val newReleaseList = getNewBooksUseCase(this) {
                if (it is UseCaseResult.Exception) {
                    throw it.t
                }
            }.await()

            val newReleaseDetailsList = newReleaseList
                .mapNotNull {
                    getBookDetailsUseCase(it, this) {
                        if (it is UseCaseResult.Exception) {
                            throw it.t
                        }
                    }.await()
                }

            assertEquals(newReleaseList.size, newReleaseDetailsList.size)

            newReleaseDetailsList
        }

        assertTrue(detailsList.isNotEmpty())
    }
}