package kr.co.hs.cleanarchitecturesample.features.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetNewBooksUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.platform.ViewModel
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel
@Inject constructor(
    private val newBooksUseCase: GetNewBooksUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private var query = ""
    val data: Flow<PagingData<BookSummaryEntity>> = Pager(
        config = PagingConfig(
            pageSize = 1,
            initialLoadSize = 3
        ),
        pagingSourceFactory = {
            if (query.isEmpty()) {
                BookNewReleasePagingSource(newBooksUseCase)
            } else {
                BookSearchResultPagingSource(searchUseCase, query)
            }
        }
    ).flow

    fun search(query: String) {
        this.query = query
    }

    fun getSearchingQuery() = this.query
}