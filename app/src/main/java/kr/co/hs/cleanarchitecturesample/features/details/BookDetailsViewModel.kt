package kr.co.hs.cleanarchitecturesample.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import kr.co.hs.cleanarchitecturesample.platform.ViewModel
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel
@Inject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) : ViewModel() {
    private val _bookDetails = MutableLiveData<BookDetailEntity>()
    val bookDetails: LiveData<BookDetailEntity> by this::_bookDetails

    fun request(isbn: String) {
        viewModelScope.launch {
            val summary = object : BookSummaryEntity {
                override val key: String = isbn
                override val title: String
                    get() = TODO("Not yet implemented")
                override val subtitle: String
                    get() = TODO("Not yet implemented")
                override val price: String
                    get() = TODO("Not yet implemented")
                override val imageUrl: URL
                    get() = TODO("Not yet implemented")

            }
            @Suppress("DeferredResultUnused")
            getBookDetailsUseCase(summary, scope = this) {
                when (it) {
                    is UseCaseResult.Error -> when (it.e) {
                        GetBookDetailsUseCase.NotFoundBookDetails ->
                            setLastError(NullPointerException("NotFoundBookDetails"))
                    }

                    is UseCaseResult.Exception -> setLastError(it.t)
                    is UseCaseResult.Success -> {
                        _bookDetails.value = it.data
                    }
                }
            }
        }
    }
}