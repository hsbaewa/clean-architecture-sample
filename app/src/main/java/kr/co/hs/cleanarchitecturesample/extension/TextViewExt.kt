package kr.co.hs.cleanarchitecturesample.extension

import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

object TextViewExt {
    fun TextView.clearSoftKeyboard() =
        with(context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager) {
            hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
}