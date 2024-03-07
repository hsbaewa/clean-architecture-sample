package kr.co.hs.cleanarchitecturesample

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.data.datasource.BookStoreDataSource
import kr.co.hs.cleanarchitecturesample.data.repository.BookStoreRepositoryImpl
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import okhttp3.OkHttpClient
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import kotlin.time.Duration

class DataTest {

    lateinit var bookStoreRepository: BookStoreRepository

    @Before
    fun init() {
        bookStoreRepository = BookStoreRepositoryImpl(
            Retrofit.Builder()
                .client(OkHttpClient.Builder().build())
                .baseUrl("https://api.itbook.store/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookStoreDataSource::class.java)
        )
    }

    @Test
    fun Book_검색_테스트() = runTest(timeout = Duration.INFINITE) {
        val searchUseCase = SearchUseCase(bookStoreRepository)
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
        val getNewBooksUseCase = GetNewBooksUseCase(bookStoreRepository)
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
    fun Book_상세_조회_테스트() = runTest {
        val detailsUseCase = GetBookDetailsUseCase(bookStoreRepository)
        var result = coroutineScope {
            // 유효하지 않은 isbn
            detailsUseCase(
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
        result = coroutineScope {
            val newRelease = GetNewBooksUseCase(bookStoreRepository)
            val summary = newRelease(this) {
                if (it is UseCaseResult.Exception) {
                    throw it.t
                }
            }.await().first()


            detailsUseCase(summary, this) {
                if (it is UseCaseResult.Exception) {
                    throw it.t
                }
            }.await()
        }

        assertNotNull(result)
    }
}