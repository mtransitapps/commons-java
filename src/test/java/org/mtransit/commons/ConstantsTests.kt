package org.mtransit.commons

import kotlin.test.Test

class ConstantsTests {

    @Test
    fun test_DEBUG() {
        val expectedDebug = (System.getenv("MT_DEBUG_DEFAULT") ?: "false").toBoolean()
        assert(Constants.DEBUG == expectedDebug)
    }
}
