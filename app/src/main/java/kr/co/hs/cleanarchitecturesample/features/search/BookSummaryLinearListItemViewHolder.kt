package kr.co.hs.cleanarchitecturesample.features.search

import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookSummaryLinearItemBinding
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.platform.ViewHolder

class BookSummaryLinearListItemViewHolder(
    private val binding: LayoutBookSummaryLinearItemBinding
) : ViewHolder<BookSummaryEntity>(binding.root) {
    override fun onBind(item: BookSummaryEntity) {
        binding.infoView.set(item)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding.infoView.dispose()
    }
}