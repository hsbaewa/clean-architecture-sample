package kr.co.hs.cleanarchitecturesample.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.features.details.BookDetailsActivity
import kr.co.hs.cleanarchitecturesample.features.search.BookSearchActivity
import java.net.URL

class NavigatorImpl : Navigator {
    override fun startSearch(context: Context) =
        context.startActivity(Intent(context, BookSearchActivity::class.java))

    override fun createBookDetailsIntent(
        context: Context,
        bookSummaryEntity: BookSummaryEntity
    ) = Intent(context, BookDetailsActivity::class.java)
        .putExtra(BookDetailsActivity.EXTRA_ISBN_13, bookSummaryEntity.key)

    override fun startDetail(context: Context, bookSummaryEntity: BookSummaryEntity) =
        context.startActivity(createBookDetailsIntent(context, bookSummaryEntity))

    override fun startUrl(context: Context, url: URL) =
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())))
}