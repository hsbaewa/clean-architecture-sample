package kr.co.hs.cleanarchitecturesample.data.model

class BookDetailsItemModel(
    val error: String?,
    val authors: String?,
    val publisher: String?,
    @Suppress("unused") val isbn10: String?,
    val pages: String?,
    val year: String?,
    val rating: String?,
    val desc: String?,
    val pdf: Map<String?, String?>?,
    title: String?,
    subtitle: String?,
    isbn13: String?,
    price: String?,
    image: String?,
    url: String?
) : BookSummaryItemModel(title, subtitle, isbn13, price, image, url) {
}