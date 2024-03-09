package kr.co.hs.cleanarchitecturesample.features.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.coroutineScope
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult

class BookSearchResultPagingSource(
    private val searchUseCase: SearchUseCase,
    private val query: String
) : PagingSource<Int, BookSummaryEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookSummaryEntity> {
        val page = params.key ?: 1
        val prevKey = params.key?.minus(1)
        val nextKey = page + 1

        return try {
            coroutineScope {
                searchUseCase(
                    query = query,
                    page = page,
                    scope = this
                ) {
                    when (it) {
                        is UseCaseResult.Error -> when (it.e) {
                            SearchUseCase.InvalidQuery -> throw IllegalArgumentException("invalid query")
                        }

                        is UseCaseResult.Exception -> throw it.t
                        is UseCaseResult.Success -> {}
                    }
                }.await()
            }.let {
                LoadResult.Page(it, prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookSummaryEntity>): Int? = null
}