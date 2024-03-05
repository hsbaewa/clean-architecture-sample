package kr.co.hs.cleanarchitecturesample.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test

class DomainTest {
    @Test
    fun assertTesT() {
        val entity = Entity("aa")
        assertEquals("aa", entity.text)
    }
}