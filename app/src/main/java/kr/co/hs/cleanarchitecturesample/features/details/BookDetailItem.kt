package kr.co.hs.cleanarchitecturesample.features.details

import java.net.URL

sealed class BookDetailItem {
    data class Title(val title: String) : BookDetailItem()
    data class SubTitle(val subTitle: String) : BookDetailItem()
    data class Authors(val authors: String) : BookDetailItem()
    data class Publisher(val publisher: String) : BookDetailItem()
    data class Pages(val pages: String) : BookDetailItem()
    data class Year(val year: String) : BookDetailItem()
    data class Rating(val rating: String) : BookDetailItem()
    data class Description(val description: String) : BookDetailItem()
    data class Price(val price: String) : BookDetailItem()
    data class Url(val url: URL) : BookDetailItem()
}