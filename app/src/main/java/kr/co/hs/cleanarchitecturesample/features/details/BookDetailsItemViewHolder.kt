package kr.co.hs.cleanarchitecturesample.features.details

import android.view.View
import kr.co.hs.cleanarchitecturesample.platform.ViewHolder

abstract class BookDetailsItemViewHolder<T : BookDetailItem>(itemView: View) :
    ViewHolder<T>(itemView)