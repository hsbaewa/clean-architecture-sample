package kr.co.hs.cleanarchitecturesample.data.model

class BookSearchResultModel(
    val total: String?,
    val page: String?,
    val books: List<BookSummaryItemModel>?
)