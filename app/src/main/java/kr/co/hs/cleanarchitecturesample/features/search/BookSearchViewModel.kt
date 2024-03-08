package kr.co.hs.cleanarchitecturesample.features.search

import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.hs.cleanarchitecturesample.domain.usecase.SearchUseCase
import kr.co.hs.cleanarchitecturesample.platform.ViewModel
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel
@Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    fun search(query: String) {}
}