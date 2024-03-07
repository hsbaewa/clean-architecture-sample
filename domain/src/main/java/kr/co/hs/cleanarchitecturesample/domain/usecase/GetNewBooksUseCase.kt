package kr.co.hs.cleanarchitecturesample.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.toList
import kr.co.hs.cleanarchitecturesample.domain.di.BookStoreRepositoryQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.repository.BookStoreRepository
import javax.inject.Inject

class GetNewBooksUseCase @Inject constructor(
    @BookStoreRepositoryQualifier
    private val repository: BookStoreRepository
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        scope: CoroutineScope = GlobalScope,
        onResult: (UseCaseResult<List<BookSummaryEntity>, Nothing>) -> Unit
    ) = scope.async {
        val result = repository
            .runCatching {
                UseCaseResult.Success(getNewBooks().catch { throw it }.toList())
            }
            .getOrElse { UseCaseResult.Exception(it) }
        onResult.invoke(result)

        return@async if (result is UseCaseResult.Success) {
            result.data
        } else emptyList()
    }
}