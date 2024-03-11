package kr.co.hs.cleanarchitecturesample.features.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.ListAdapter
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailDividerItemBinding
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailRatingItemBinding
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookDetailTextItemBinding

class BookDetailItemListAdapter(
    private val onItemClick: (BookDetailItem) -> Unit
) : ListAdapter<BookDetailItem, BookDetailsItemViewHolder<BookDetailItem>>(
    BookDetailItemDiffCallback()
) {

    companion object {
        const val VT_TITLE = 1
        const val VT_SUB_TITLE = 2
        const val VT_AUTHORS = 3
        const val VT_DESC = 4
        const val VT_PAGES = 5
        const val VT_PRICE = 6
        const val VT_PUBLISHER = 7
        const val VT_URL = 8
        const val VT_YEAR = 9
        const val VT_IMAGE_URL = 11
        const val VT_RATING = 10
        const val VT_DIVIDER = 12
        const val VT_PREVIEW = 13

    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        value = [
            VT_TITLE,
            VT_SUB_TITLE,
            VT_AUTHORS,
            VT_DESC,
            VT_PAGES,
            VT_PRICE,
            VT_PUBLISHER,
            VT_URL,
            VT_YEAR,
            VT_RATING,
            VT_DIVIDER,
            VT_PREVIEW
        ]
    )
    private annotation class ViewType

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @ViewType viewType: Int
    ): BookDetailsItemViewHolder<BookDetailItem> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VT_TITLE,
            VT_SUB_TITLE,
            VT_AUTHORS,
            VT_DESC,
            VT_PAGES,
            VT_PRICE,
            VT_PUBLISHER,
            VT_URL,
            VT_YEAR,
            VT_PREVIEW ->
                BookDetailTextItemViewHolder(
                    LayoutBookDetailTextItemBinding.inflate(inflater, parent, false)
                ).apply {
                    itemView.setOnClickListener { onItemClick(getItem(bindingAdapterPosition)) }
                }

            VT_RATING ->
                BookDetailsRatingItemViewHolder(
                    LayoutBookDetailRatingItemBinding.inflate(inflater, parent, false)
                )

            VT_DIVIDER ->
                BookDetailsPreviewTitleViewHolder(
                    LayoutBookDetailDividerItemBinding.inflate(inflater, parent, false)
                )

            else -> throw Exception("invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: BookDetailsItemViewHolder<BookDetailItem>,
        position: Int
    ) {
        holder.onBind(getItem(position))
    }

    override fun onViewRecycled(holder: BookDetailsItemViewHolder<BookDetailItem>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    @ViewType
    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is BookDetailItem.Authors -> VT_AUTHORS
        is BookDetailItem.Description -> VT_DESC
        is BookDetailItem.Pages -> VT_PAGES
        is BookDetailItem.Price -> VT_PRICE
        is BookDetailItem.Publisher -> VT_PUBLISHER
        is BookDetailItem.Rating -> VT_RATING
        is BookDetailItem.SubTitle -> VT_SUB_TITLE
        is BookDetailItem.Title -> VT_TITLE
        is BookDetailItem.Url -> VT_URL
        is BookDetailItem.Year -> VT_YEAR
        is BookDetailItem.ImageUrl -> VT_IMAGE_URL
        is BookDetailItem.PreviewHeader -> VT_DIVIDER
        is BookDetailItem.Preview -> VT_PREVIEW
    }
}