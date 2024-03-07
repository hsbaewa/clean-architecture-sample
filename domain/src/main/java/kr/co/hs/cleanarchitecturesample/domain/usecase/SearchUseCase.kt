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

class SearchUseCase @Inject constructor(
    @BookStoreRepositoryQualifier
    private val repository: BookStoreRepository
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        query: String,
        page: Int? = null,
        scope: CoroutineScope = GlobalScope,
        onResult: (UseCaseResult<List<BookSummaryEntity>, Error>) -> Unit
    ) = scope.async {
        val result = when {
            query.isEmpty() -> UseCaseResult.Error(InvalidQuery)
            else -> repository
                .runCatching {
                    val flow = page?.let { search(query, it) } ?: search(query)
                    UseCaseResult.Success(flow.catch { throw it }.toList())
                }
                .getOrElse { UseCaseResult.Exception(it) }
        }

        onResult.invoke(result)

        if (result is UseCaseResult.Success) {
            result.data
        } else emptyList()
    }

    sealed interface Error
    data object InvalidQuery : Error
}