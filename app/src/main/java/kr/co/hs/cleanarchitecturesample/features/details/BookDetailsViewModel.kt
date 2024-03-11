package kr.co.hs.cleanarchitecturesample.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.domain.usecase.GetBookDetailsUseCase
import kr.co.hs.cleanarchitecturesample.domain.usecase.UseCaseResult
import kr.co.hs.cleanarchitecturesample.platform.ViewModel
import java.net.URL
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel
@Inject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase
) : ViewModel() {
    private val _bookDetails = MutableLiveData<Set<BookDetailItem>>()
    val bookDetails: LiveData<Set<BookDetailItem>> by this::_bookDetails

    fun request(isbn: String) {
        viewModelScope.launch {
            val summary = object : BookSummaryEntity {
                override val key: String = isbn
                override val title: String = ""
                override val subtitle: String = ""
                override val price: String = ""
                override val imageUrl: URL? = null

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
                        _bookDetails.value = it.data.run {
                            TreeSet<BookDetailItem>().also { set ->
                                price.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Price(this)) }
                                desc.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Description(this)) }
                                rating.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Rating(this)) }
                                year.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Year(this)) }
                                pages.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Pages(this)) }
                                publisher.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Publisher(this)) }
                                authors.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Authors(this)) }
                                subtitle.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.SubTitle(this)) }
                                title.takeIf { it.isNotEmpty() }
                                    ?.apply { set.add(BookDetailItem.Title(this)) }
                                url?.let { url -> set.add(BookDetailItem.Url(url.toString())) }
                                imageUrl?.let { imageUrl -> set.add(BookDetailItem.ImageUrl(imageUrl)) }
                            }
                        }
                    }
                }
            }
        }
    }
}