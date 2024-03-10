package kr.co.hs.cleanarchitecturesample.features.search

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity

class BookSummaryEntityDiffCallback : DiffUtil.ItemCallback<BookSummaryEntity>() {
    override fun areItemsTheSame(oldItem: BookSummaryEntity, newItem: BookSummaryEntity): Boolean {
        return oldItem.key == newItem.key
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: BookSummaryEntity,
        newItem: BookSummaryEntity
    ): Boolean {
        return oldItem == newItem
    }
}