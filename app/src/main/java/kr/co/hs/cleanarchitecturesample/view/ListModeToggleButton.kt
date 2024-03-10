package kr.co.hs.cleanarchitecturesample.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IntDef
import com.google.android.material.button.MaterialButton
import kr.co.hs.cleanarchitecturesample.R

class ListModeToggleButton : MaterialButton {
    companion object {
        const val MODE_LINEAR = 1
        const val MODE_GRID = 2
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setIconResource(R.drawable.baseline_view_list_24)
    }

    @IntDef(value = [MODE_LINEAR, MODE_GRID])
    annotation class ListMode

    @ListMode
    var listMode: Int = MODE_LINEAR
        set(value) {
            when (value) {
                MODE_LINEAR -> setIconResource(R.drawable.baseline_view_list_24)
                MODE_GRID -> setIconResource(R.drawable.baseline_apps_24)
            }
            field = value
        }
        @ListMode get

    fun setOnToggleListener(onToggleListener: OnToggleListener?) {
        this.onToggleListener = onToggleListener
    }

    private var onToggleListener: OnToggleListener? = null

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        toggleListMode()
        onToggleListener?.onChangedMode(listMode)
    }

    private fun toggleListMode() {
        when (listMode) {
            MODE_LINEAR -> listMode = MODE_GRID
            MODE_GRID -> listMode = MODE_LINEAR
        }
    }

    interface OnToggleListener {
        fun onChangedMode(@ListMode listMode: Int)
    }

}