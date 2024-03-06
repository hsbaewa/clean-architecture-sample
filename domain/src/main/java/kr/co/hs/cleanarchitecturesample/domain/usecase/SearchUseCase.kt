package kr.co.hs.cleanarchitecturesample.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository

class SearchUseCase(
    private val repository: BookStoreRepository
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        query: String,
        page: Int? = null,
        scope: CoroutineScope = GlobalScope,
        onResult: (UseCaseResult<List<BookSummaryEntity>, Error>) -> Unit
    ) {
        if (query.isEmpty()) {
            onResult(UseCaseResult.Error(InvalidQuery))
        }

        scope.launch {
            onResult(
                repository
                    .runCatching {
                        val flow = page?.let { search(query, it) } ?: search(query)
                        UseCaseResult.Success(flow.catch { throw it }.toList())
                    }
                    .getOrElse {
                        UseCaseResult.Exception(it)
                    }
            )
        }
    }

    sealed interface Error
    data object InvalidQuery : Error
}