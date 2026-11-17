package org.mtransit.commons

import kotlin.test.Test
import kotlin.test.assertTrue

class CharUtilsTest {

    @Test
    fun test_isUppercaseOnly() {
        CharUtils.isUppercaseOnly("STACKHOUSE @ FANSHAWE NB - #2910", allowWhitespace = true, checkAZOnly = true).let { result ->
            assertTrue { result }
        }
        CharUtils.isUppercaseOnly("YMCA", allowWhitespace = true, checkAZOnly = true).let { result ->
            assertTrue { result }
        }
    }
}
