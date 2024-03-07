package kr.co.hs.cleanarchitecturesample

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class DomainTest {
    lateinit var bookStoreRepository: BookStoreRepository

    @Before
    fun init() {
        bookStoreRepository = TestBookStoreRepository()
    }

    @Test
    fun Book_검색_테스트() = runTest {
        val search = SearchUseCase(bookStoreRepository)

        assertEquals(
            2,
            coroutineScope {
                val resultList = ArrayList<BookSummaryEntity>()
                search(
                    query = "제목",
                    scope = this@coroutineScope,
                    onResult = {
                        when (it) {
                            is UseCaseResult.Error -> when (it.e) {
                                SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                            }

                            is UseCaseResult.Success -> resultList.addAll(it.data)
                            is UseCaseResult.Exception -> throw it.t
                        }
                    }
                )
                resultList
            }.size
        )

        assertEquals(
            1,
            coroutineScope {
                val resultList = ArrayList<BookSummaryEntity>()
                search(
                    query = "제목",
                    page = 2,
                    scope = this@coroutineScope,
                    onResult = {
                        when (it) {
                            is UseCaseResult.Error -> when (it.e) {
                                SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                            }

                            is UseCaseResult.Success -> resultList.addAll(it.data)
                            is UseCaseResult.Exception -> throw it.t
                        }
                    }
                )
                resultList
            }.size
        )

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                coroutineScope {
                    search(
                        query = "",
                        scope = this@coroutineScope,
                        onResult = {
                            when (it) {
                                is UseCaseResult.Error -> when (it.e) {
                                    SearchUseCase.InvalidQuery -> throw IllegalArgumentException("InvalidQuery")
                                }

                                is UseCaseResult.Success -> {}
                                is UseCaseResult.Exception -> {}
                            }
                        }
                    )
                }
            }
        }

    }

    @Test
    fun Book_신규_요청_테스트() = runTest {
        val request = GetNewBooksUseCase(bookStoreRepository)

        assertEquals(
            1,
            coroutineScope {
                val resultList = ArrayList<BookSummaryEntity>()
                request(
                    scope = this@coroutineScope,
                    onResult = {
                        when (it) {
                            is UseCaseResult.Error -> {}
                            is UseCaseResult.Exception -> throw it.t
                            is UseCaseResult.Success -> resultList.addAll(it.data)
                        }
                    }
                )
                resultList
            }.size
        )
    }

    @Test
    fun Book_상세_요청_테스트() = runTest {
        val search = SearchUseCase(bookStoreRepository)
        val request = GetBookDetailsUseCase(bookStoreRepository)

        val searchList = coroutineScope {
            val resultList = ArrayList<BookSummaryEntity>()
            search(
                "제목1",
                scope = this,
                onResult = {
                    when (it) {
                        is UseCaseResult.Error -> when (it.e) {
                            SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                        }

                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {
                            resultList.addAll(it.data)
                        }
                    }
                }
            )

            resultList
        }

        assertEquals(1, searchList.size)
        val summary = searchList.first()

        coroutineScope {
            request(
                summary,
                scope = this,
                onResult = {
                    when (it) {
                        is UseCaseResult.Error -> {}
                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {
                            assertEquals(it.data.subtitle, summary.subtitle)
                        }
                    }
                }
            )
        }
    }
}