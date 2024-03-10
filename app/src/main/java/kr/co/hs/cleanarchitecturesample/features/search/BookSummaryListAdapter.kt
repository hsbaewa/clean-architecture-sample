package kr.co.hs.cleanarchitecturesample.features.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.paging.PagingDataAdapter
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookSummaryGridItemBinding
import kr.co.hs.cleanarchitecturesample.databinding.LayoutBookSummaryLinearItemBinding
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.platform.ViewHolder

class BookSummaryListAdapter(
    private val onItemClick: (BookSummaryEntity) -> Unit
) : PagingDataAdapter<BookSummaryEntity, ViewHolder<BookSummaryEntity>>(
    BookSummaryEntityDiffCallback()
) {
    companion object {
        const val VT_LINEAR = 1
        const val VT_GRID = 2
    }

    @IntDef(value = [VT_LINEAR, VT_GRID])
    annotation class ViewType

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @ViewType viewType: Int
    ): ViewHolder<BookSummaryEntity> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VT_LINEAR -> {
                val binding = LayoutBookSummaryLinearItemBinding.inflate(inflater, parent, false)
                BookSummaryLinearListItemViewHolder(binding)
                    .apply {
                        binding.infoView.setOnClickListener {
                            getItem(bindingAdapterPosition)?.let(onItemClick)
                        }
                    }
            }

            VT_GRID -> {
                val binding = LayoutBookSummaryGridItemBinding.inflate(inflater, parent, false)
                BookSummaryGridListItemViewHolder(binding)
                    .apply {
                        binding.infoView.setOnClickListener {
                            getItem(bindingAdapterPosition)?.let(onItemClick)
                        }
                    }
            }

            else -> throw Exception("invalid view type")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder<BookSummaryEntity>, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onViewRecycled(holder: ViewHolder<BookSummaryEntity>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    @ViewType
    var viewType: Int = VT_LINEAR

    @ViewType
    override fun getItemViewType(position: Int): Int = viewType
}