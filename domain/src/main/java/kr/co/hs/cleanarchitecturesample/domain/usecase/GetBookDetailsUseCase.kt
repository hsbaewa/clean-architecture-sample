package kr.co.hs.cleanarchitecturesample.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository

class GetBookDetailsUseCase(
    private val repository: BookStoreRepository
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        summary: BookSummaryEntity,
        scope: CoroutineScope = GlobalScope,
        onResult: (UseCaseResult<BookDetailEntity, Throwable>) -> Unit
    ) {
        scope.launch {
            onResult(
                repository
                    .runCatching {
                        UseCaseResult.Success(
                            getBookDetails(summary)
                                .catch { throw it }
                                .first()
                        )
                    }
                    .getOrElse { UseCaseResult.Error(it) }
            )
        }
    }
}