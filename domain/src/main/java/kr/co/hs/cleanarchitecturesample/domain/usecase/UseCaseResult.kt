package kr.co.hs.cleanarchitecturesample.domain.usecase

sealed class UseCaseResult<out T, out E> {
    data class Success<T>(val data: T) : UseCaseResult<T, Nothing>()
    data class Error<E>(val e: E) : UseCaseResult<Nothing, E>()
    data class Exception(val t: Throwable) : UseCaseResult<Nothing, Nothing>()
}