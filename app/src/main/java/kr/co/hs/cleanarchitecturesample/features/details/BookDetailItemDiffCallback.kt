package kr.co.hs.cleanarchitecturesample.features.details

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class BookDetailItemDiffCallback : DiffUtil.ItemCallback<BookDetailItem>() {
    override fun areItemsTheSame(oldItem: BookDetailItem, newItem: BookDetailItem): Boolean =
        oldItem.order == newItem.order

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BookDetailItem, newItem: BookDetailItem): Boolean =
        oldItem == newItem
}