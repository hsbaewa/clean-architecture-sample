package kr.co.hs.cleanarchitecturesample.platform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import kr.co.hs.cleanarchitecturesample.extension.LiveDataExt.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class ViewModelTest {
    /**
     * rules
     */
    @get:Rule
    var instantTaskExecutorRule =
        InstantTaskExecutorRule() // 테스트시 스레드 동기화 coroutineScope 가 한 쓰레드에서 동작

    @Before
    fun init() {
        // viewModelScope 또는 lifeCycleScope가 동작할 쓰레드 설정.
        Dispatchers.setMain(newSingleThreadContext(ViewModelTest::class.java.simpleName))
    }

    @Test
    fun testViewModel() {
        val vm = ViewModelImpl()
        vm.newError()
        val e = vm.lastError.getOrAwaitValue(time = 10)
        assertEquals("e", e.message)
    }

    private class ViewModelImpl : ViewModel() {
        fun newError() = viewModelScope.launch {
            setLastError(Exception("e"))
        }
    }
}