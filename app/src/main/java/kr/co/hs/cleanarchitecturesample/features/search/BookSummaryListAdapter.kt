package kr.co.hs.cleanarchitecturesample.features.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookSummaryLinearItemBinding
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity

class BookSummaryListAdapter :
    PagingDataAdapter<BookSummaryEntity, BookSummaryLinearListItemViewHolder>(
        BookSummaryEntityDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookSummaryLinearListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutBookSummaryLinearItemBinding.inflate(inflater, parent, false)
        return BookSummaryLinearListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookSummaryLinearListItemViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onViewRecycled(holder: BookSummaryLinearListItemViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}