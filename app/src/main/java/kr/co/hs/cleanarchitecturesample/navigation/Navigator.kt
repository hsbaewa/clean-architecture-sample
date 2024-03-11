package kr.co.hs.cleanarchitecturesample.navigation

import android.content.Context
import android.content.Intent
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import java.net.URL

interface Navigator {
    fun startSearch(context: Context)
    fun createBookDetailsIntent(context: Context, bookSummaryEntity: BookSummaryEntity): Intent
    fun startDetail(context: Context, bookSummaryEntity: BookSummaryEntity)
    fun startUrl(context: Context, url: URL)
}