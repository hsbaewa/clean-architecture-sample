package kr.co.hs.cleanarchitecturesample.platform

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(item: T)
    open fun onViewRecycled() {}
    open fun onViewAttachedToWindow() {}
    open fun onViewDetachedFromWindow() {}

    protected fun getString(resId: Int) = itemView.context.getString(resId)
}