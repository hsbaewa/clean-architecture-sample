package kr.co.hs.cleanarchitecturesample.features.details

import android.view.View
import kr.co.hs.cleanarchitecturesample.platform.ViewHolder

abstract class BookDetailItemViewHolder<T : BookDetailItem>(itemView: View) :
    ViewHolder<T>(itemView)