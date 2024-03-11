package kr.co.hs.cleanarchitecturesample.features.details

import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailRatingItemBinding

class BookDetailRatingItemViewHolder(private val binding: LayoutBookDetailRatingItemBinding) :
    BookDetailItemViewHolder<BookDetailItem>(binding.root) {
    override fun onBind(item: BookDetailItem) {
        if (item is BookDetailItem.Rating) {
            binding.tvLabel.text = getString(R.string.details_item_label_rating)
            binding.rating.rating = item.floatRating
        }
    }
}