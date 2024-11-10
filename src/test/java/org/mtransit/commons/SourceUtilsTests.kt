package org.mtransit.commons

import org.junit.Assert.assertEquals
import org.junit.Test

class SourceUtilsTests {

    @Test
    fun `test label from URL - www_example_com_00_abc_test_2`() {
        val urlString = "https://www.example.com:00/abc?test=2"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - www_example_com`() {
        val urlString = "https://www.example.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - www_example_qc_ca`() {
        val urlString = "https://api.example.qc.ca"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.qc.ca", result)
    }
}
