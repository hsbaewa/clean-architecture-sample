package kr.co.hs.cleanarchitecturesample.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import coil.dispose
import coil.load
import com.google.android.material.card.MaterialCardView
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity

class BookInfoGridItemView: MaterialCardView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.view_book_info_grid_item, this)
    }

    private val ivImage: ImageView by lazy { findViewById(R.id.iv_image) }
    private val tvTitle: TextView by lazy { findViewById(R.id.tv_title) }
    private val tvSubTitle: TextView by lazy { findViewById(R.id.tv_sub_title) }
    private val tvPrice: TextView by lazy { findViewById(R.id.tv_price) }

    fun set(entity: BookSummaryEntity) {
        ivImage.load(entity.imageUrl.toString()) { crossfade(true) }
        tvTitle.text = entity.title
        tvSubTitle.text = entity.subtitle
        tvPrice.text = entity.price
    }

    fun dispose() = ivImage.dispose()
}