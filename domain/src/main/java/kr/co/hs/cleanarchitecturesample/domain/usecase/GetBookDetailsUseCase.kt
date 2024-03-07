package kr.co.hs.cleanarchitecturesample.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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
        onResult: (UseCaseResult<BookDetailEntity, Error>) -> Unit
    ) = scope.async {
        val res = repository
            .runCatching {
                UseCaseResult.Success(getBookDetails(summary).catch { throw it }.first())
            }
            .getOrElse {
                when (it) {
                    is NullPointerException -> UseCaseResult.Error(NotFoundBookDetails)
                    else -> UseCaseResult.Exception(it)
                }
            }
        onResult.invoke(res)

        return@async if (res is UseCaseResult.Success) {
            res.data
        } else null
    }

    sealed interface Error
    data object NotFoundBookDetails : Error
}