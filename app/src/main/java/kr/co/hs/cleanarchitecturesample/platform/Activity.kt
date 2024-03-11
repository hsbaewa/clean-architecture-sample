package kr.co.hs.cleanarchitecturesample.platform

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.hs.cleanarchitecturesample.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class Activity : AppCompatActivity() {
    fun showThrowable(t: Throwable): AlertDialog = MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(
            when (t) {
                is UnknownHostException -> getString(R.string.error_message_connect)
                is SocketTimeoutException -> getString(R.string.error_message_timeout)
                else -> t.message
            }
        )
        .setPositiveButton(R.string.common_confirm, null)
        .show()
}