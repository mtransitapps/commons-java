package org.mtransit.commons

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.Pattern

class CleanerTest {

    @Test
    fun test_compile_CASE_INSENSITIVE() {
        val cleaner = Cleaner.compile("abc", Pattern.CASE_INSENSITIVE)

        assertTrue(cleaner.regex.options.contains(RegexOption.IGNORE_CASE))
    }

    @Test
    fun test_compile_default() {
        val cleaner = Cleaner.compile("abc")

        assertFalse(cleaner.regex.options.contains(RegexOption.IGNORE_CASE))
    }

    @Test
    fun test_compile_default_other() {
        val cleaner = Cleaner.compile("abc", 1)

        assertFalse(cleaner.regex.options.contains(RegexOption.IGNORE_CASE))
    }
}