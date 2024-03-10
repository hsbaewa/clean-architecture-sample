package kr.co.hs.cleanarchitecturesample.features.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.coroutineScope
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult

class BookNewReleasePagingSource(
    private val getNewBooksUseCase: GetNewBooksUseCase
) : PagingSource<Int, BookSummaryEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookSummaryEntity> {
        return try {
            coroutineScope {
                getNewBooksUseCase(scope = this) {
                    when (it) {
                        is UseCaseResult.Error -> throw Exception("unknown error")
                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {}
                    }
                }.await()
            }.let { LoadResult.Page(it, null, null) }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookSummaryEntity>): Int? = null
}