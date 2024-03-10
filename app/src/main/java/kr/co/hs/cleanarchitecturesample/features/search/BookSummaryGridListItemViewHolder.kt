package kr.co.hs.cleanarchitecturesample.features.search

import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookSummaryGridItemBinding
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.platform.ViewHolder

class BookSummaryGridListItemViewHolder(
    private val binding: LayoutBookSummaryGridItemBinding
) : ViewHolder<BookSummaryEntity>(binding.root) {
    override fun onBind(item: BookSummaryEntity) {
        binding.infoView.set(item)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding.infoView.dispose()
    }
}