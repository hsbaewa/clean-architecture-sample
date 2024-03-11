package kr.co.hs.cleanarchitecturesample.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kr.co.hs.cleanarchitecturesample.data.Mapper.toDomain
import kr.co.hs.cleanarchitecturesample.data.datasource.BookStoreDataSource
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository

class BookStoreRepositoryImpl(
    private val bookStoreDataSource: BookStoreDataSource
) : BookStoreRepository {
    override fun search(query: String): Flow<BookSummaryEntity> =
        flow {
            val response = withContext(Dispatchers.IO) { bookStoreDataSource.search(query) }
            with(response) {
                if (isSuccessful) {
                    body()?.books?.mapNotNull { it.toDomain() }
                        ?: throw NullPointerException("request success, but response body is null")
                } else {
                    throw Exception(errorBody()?.string() ?: "")
                }
            }.forEach { emit(it) }
        }

    override fun search(query: String, page: Int): Flow<BookSummaryEntity> =
        flow {
            val response = withContext(Dispatchers.IO) { bookStoreDataSource.search(query, page) }
            with(response) {
                if (isSuccessful) {
                    body()?.books?.mapNotNull { it.toDomain() }
                        ?: throw NullPointerException("request success, but response body is null")
                } else {
                    throw Exception(errorBody()?.string() ?: "")
                }
            }.forEach { emit(it) }
        }

    override fun getNewBooks(): Flow<BookSummaryEntity> =
        flow {
            val response = withContext(Dispatchers.IO) { bookStoreDataSource.new() }
            with(response) {
                if (isSuccessful) {
                    body()?.books?.mapNotNull { it.toDomain() }
                        ?: throw NullPointerException("request success, but response body is null")
                } else {
                    throw Exception(errorBody()?.string() ?: "")
                }
            }.forEach { emit(it) }
        }

    override fun getBookDetails(bookSummaryEntity: BookSummaryEntity): Flow<BookDetailEntity> =
        flow {
            val response =
                withContext(Dispatchers.IO) { bookStoreDataSource.details(bookSummaryEntity.key) }
            with(response) {
                if (isSuccessful) {
                    body()?.toDomain()
                        ?: throw NullPointerException("request success, but response body is null")
                } else {
                    if (this.code() == 404) {
                        throw NullPointerException("not found")
                    } else {
                        throw Exception(errorBody()?.string() ?: "")
                    }
                }
            }.let { emit(it) }
        }
}