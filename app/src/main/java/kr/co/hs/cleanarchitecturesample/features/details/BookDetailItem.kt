package kr.co.hs.cleanarchitecturesample.features.details

import java.net.URL

sealed class BookDetailItem : Comparable<BookDetailItem> {

    abstract val id: String
    abstract val order: Int

    override fun compareTo(other: BookDetailItem): Int =
        order.compareTo(other.order)

    sealed interface BookDetailTextItem {
        val value: String
    }

    data class Title(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 0
    }

    data class SubTitle(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 1
    }

    data class Authors(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 2
    }

    data class Publisher(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 3
    }

    data class Pages(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 4
    }

    data class Year(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 5
    }

    data class Rating(val rating: String) : BookDetailItem() {
        override val id: String
            get() = "rating:$rating"

        override val order: Int = 6
        val floatRating: Float
            get() = rating.toFloatOrNull() ?: 0f
    }

    data class Description(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 7
    }

    data class Price(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 8
    }

    data class Url(override val value: String) : BookDetailItem(), BookDetailTextItem {
        override val id: String
            get() = value
        override val order: Int = 9
    }

    data class ImageUrl(val url: URL) : BookDetailItem() {
        override val id: String
            get() = url.toString()
        override val order: Int = 10
    }
}