package kr.co.hs.cleanarchitecturesample.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ViewModel : ViewModel() {
    private val _lastError = MutableLiveData<Throwable>()
    val lastError: LiveData<Throwable> by ::_lastError

    protected fun setLastError(t: Throwable) {
        _lastError.value = t
    }
}