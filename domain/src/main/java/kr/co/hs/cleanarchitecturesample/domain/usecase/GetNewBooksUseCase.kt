package kr.co.hs.cleanarchitecturesample.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository

class GetNewBooksUseCase(
    private val repository: BookStoreRepository
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        scope: CoroutineScope = GlobalScope,
        onResult: (UseCaseResult<List<BookSummaryEntity>, Nothing>) -> Unit
    ) {
        scope.launch {
            onResult(
                repository
                    .runCatching {
                        UseCaseResult.Success(getNewBooks().catch { throw it }.toList())
                    }
                    .getOrElse { UseCaseResult.Exception(it) }
            )
        }
    }
}