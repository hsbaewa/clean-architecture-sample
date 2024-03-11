package kr.co.hs.cleanarchitecturesample.domain.entities

import java.net.URL

interface BookDetailEntity : BookSummaryEntity {
    val authors: String
    val publisher: String
    val pages: String
    val year: String
    val rating: String
    val desc: String
    val url: URL?
    val preview: List<BookPreviewEntity>
}