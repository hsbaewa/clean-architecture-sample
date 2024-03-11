package kr.co.hs.cleanarchitecturesample.features.details

import android.graphics.Paint
import androidx.core.content.ContextCompat
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailTextItemBinding

class BookDetailTextItemViewHolder(val binding: LayoutBookDetailTextItemBinding) :
    BookDetailsItemViewHolder<BookDetailItem>(binding.root) {
    override fun onBind(item: BookDetailItem) {
        binding.tvLabel.text = when (item as BookDetailItem.BookDetailTextItem) {
            is BookDetailItem.Title -> getString(R.string.details_item_label_title)
            is BookDetailItem.Authors -> getString(R.string.details_item_label_authors)
            is BookDetailItem.Description -> getString(R.string.details_item_label_description)
            is BookDetailItem.Pages -> getString(R.string.details_item_label_pages)
            is BookDetailItem.Price -> getString(R.string.details_item_label_price)
            is BookDetailItem.Publisher -> getString(R.string.details_item_label_publisher)
            is BookDetailItem.SubTitle -> getString(R.string.details_item_label_subtitle)
            is BookDetailItem.Url -> getString(R.string.details_item_label_url)
            is BookDetailItem.Year -> getString(R.string.details_item_label_year)
            is BookDetailItem.Preview -> (item as BookDetailItem.Preview).previewEntity.label
        }

        with(binding.tvText) {
            text = item.value
            if (paintFlags and Paint.UNDERLINE_TEXT_FLAG == Paint.UNDERLINE_TEXT_FLAG) {
                paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
            when (item) {
                is BookDetailItem.Url,
                is BookDetailItem.Preview -> {
                    paintFlags = binding.tvText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                }

                else -> {
                    setTextColor(ContextCompat.getColor(context, R.color.gray))
                }
            }
        }

    }
}