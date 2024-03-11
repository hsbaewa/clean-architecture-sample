package kr.co.hs.cleanarchitecturesample.domain.entities

import java.net.URL

interface BookPreviewEntity {
    val label: String
    val url: URL
}