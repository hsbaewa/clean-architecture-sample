package kr.co.hs.cleanarchitecturesample.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity

interface BookStoreRepository {
    fun search(query: String): Flow<BookSummaryEntity>
    fun search(query: String, page: Int): Flow<BookSummaryEntity>
    fun getNewBooks(): Flow<BookSummaryEntity>
    fun getBookDetails(bookSummaryEntity: BookSummaryEntity): Flow<BookDetailEntity>
}