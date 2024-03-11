package kr.co.hs.cleanarchitecturesample.extension

import android.content.res.Resources
import android.util.TypedValue

object NumberExt {
    val Int.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
}