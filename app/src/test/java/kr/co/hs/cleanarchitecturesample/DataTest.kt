package kr.co.hs.cleanarchitecturesample

import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kr.co.hs.cleanarchitecturesample.data.datasource.BookStoreDataSource
import kr.co.hs.cleanarchitecturesample.data.repository.BookStoreRepositoryImpl
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    fun Book_검색_테스트() = runTest {
        bookStoreRepository.search("mongodb")
        val searchUseCase = SearchUseCase(bookStoreRepository)

        val searchResult = coroutineScope {
            val searchResult = ArrayList<BookSummaryEntity>()
            searchUseCase("mongodb", scope = this, onResult = {
                when (it) {
                    is UseCaseResult.Error -> when (it.e) {
                        SearchUseCase.InvalidQuery -> throw Exception("InvalidQuery")
                    }

                    is UseCaseResult.Exception -> throw it.t
                    is UseCaseResult.Success -> searchResult.addAll(it.data)
                }
            })
            searchResult
        }

        assertTrue(searchResult.isNotEmpty())
    }
}