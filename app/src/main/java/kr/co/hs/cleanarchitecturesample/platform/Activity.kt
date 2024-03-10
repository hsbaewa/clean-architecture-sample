package kr.co.hs.cleanarchitecturesample.platform

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.hs.cleanarchitecturesample.R

abstract class Activity : AppCompatActivity() {
    fun showThrowable(t: Throwable) = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(t.message)
        .setPositiveButton(R.string.common_confirm, null)
        .show()
}