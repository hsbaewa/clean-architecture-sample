package kr.co.hs.cleanarchitecturesample.data.model

internal class BookSearchResultModel(
    val total: String?,
    val page: String?,
    val books: List<BookSummaryItemModel>?
)