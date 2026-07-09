package org.mtransit.commons

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstantsTests {

    @Test
    fun test_DEBUG() {
        val expectedDebug = (System.getenv("MT_DEBUG_DEFAULT") ?: "false").toBoolean()
        assertEquals(expectedDebug, Constants.DEBUG)
    }
}
