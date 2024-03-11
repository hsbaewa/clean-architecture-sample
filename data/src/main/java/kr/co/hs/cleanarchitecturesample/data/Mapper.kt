package kr.co.hs.cleanarchitecturesample.data

import kr.co.hs.cleanarchitecturesample.data.model.BookDetailsItemModel
import kr.co.hs.cleanarchitecturesample.data.model.BookSummaryItemModel
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookPreviewEntity
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import java.net.URL

object Mapper {
    fun BookSummaryItemModel.toDomain(): BookSummaryEntity? {
        val key = isbn13 ?: return null
        val title = title ?: return null
        val subtitle = subtitle ?: ""
        val price = price ?: "$0"
        val imageUrl = image?.runCatching { URL(this) }?.getOrNull()
        return object : BookSummaryEntity {
            override val key: String = key
            override val title: String = title
            override val subtitle: String = subtitle
            override val price: String = price
            override val imageUrl: URL? = imageUrl
        }
    }

    fun BookDetailsItemModel.toDomain(): BookDetailEntity? {
        val key = isbn13 ?: return null
        val title = title ?: return null
        val subtitle = subtitle ?: ""
        val price = price ?: "$0"
        val imageUrl = image?.runCatching { URL(this) }?.getOrNull()

        val authors = authors ?: ""
        val publisher = publisher ?: ""
        val pages = pages ?: "0"
        val year = year ?: ""
        val rating = rating ?: "0"
        val desc = desc ?: ""
        val url = url?.runCatching { URL(this) }?.getOrNull()

        val preview = pdf?.mapNotNull {
            val label = it.key ?: return@mapNotNull null
            val previewUrl =
                it.value?.runCatching { URL(this) }?.getOrNull() ?: return@mapNotNull null
            object : BookPreviewEntity {
                override val label: String = label
                override val url: URL = previewUrl
            }
        } ?: emptyList()

        return object : BookDetailEntity {
            override val authors: String = authors
            override val publisher: String = publisher
            override val pages: String = pages
            override val year: String = year
            override val rating: String = rating
            override val desc: String = desc
            override val url: URL? = url
            override val key: String = key
            override val title: String = title
            override val subtitle: String = subtitle
            override val price: String = price
            override val imageUrl: URL? = imageUrl
            override val preview: List<BookPreviewEntity> = preview
        }
    }
}