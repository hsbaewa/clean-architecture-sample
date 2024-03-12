package kr.co.hs.cleanarchitecturesample.features.details

import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailDividerItemBinding

class BookDetailsPreviewTitleViewHolder(private val binding: LayoutBookDetailDividerItemBinding) :
    BookDetailsItemViewHolder<BookDetailItem>(binding.root) {
    override fun onBind(item: BookDetailItem) {
        binding.tvHeader.text = getString(R.string.details_item_label_preview)
    }
}