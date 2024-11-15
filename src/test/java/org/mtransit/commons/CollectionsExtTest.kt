package org.mtransit.commons

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionsExtTest {

    @Test
    fun test_dropWhile_minSize() {
        val subject = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        val result = subject.dropWhile(minSize = 6) { it < 5 }

        assertEquals(listOf(4, 5, 6, 7, 8, 9), result)
    }
}