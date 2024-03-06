package kr.co.hs.cleanarchitecturesample.domain.entities

import java.net.URL

interface BookSummaryEntity {
    val key: String
    val title: String
    val subtitle: String
    val price: String
    val imageUrl: URL?
}