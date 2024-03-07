package kr.co.hs.cleanarchitecturesample.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.hs.cleanarchitecturesample.data.Mapper.toDomain
import kr.co.hs.cleanarchitecturesample.data.datasource.BookStoreDataSource
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository

internal class BookStoreRepositoryImpl(
    private val bookStoreDataSource: BookStoreDataSource
) : BookStoreRepository {
    override fun search(query: String): Flow<BookSummaryEntity> =
        flow {
            with(bookStoreDataSource.search(query)) {
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
            with(bookStoreDataSource.search(query, page)) {
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
            with(bookStoreDataSource.new()) {
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
            with(bookStoreDataSource.details(bookSummaryEntity.key)) {
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