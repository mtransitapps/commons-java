package org.mtransit.commons

import org.junit.Assert.assertEquals
import org.junit.Test

class SourceUtilsTests {

    @Test
    fun `test label from URL - null`() {
        assertEquals(null, SourceUtils.getSourceLabel(urlString = null))

        assertEquals(null, SourceUtils.getSourceLabel(url = null))
    }

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
    fun `test label from URL - api_example_qc_ca`() {
        val urlString = "https://api.example.qc.ca"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.qc.ca", result)
    }

    @Test
    fun `test label from URL - api_example_com`() {
        val urlString = "https://api.example.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - opendata_example_qc_ca`() {
        val urlString = "https://opendata.example.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - gtfs_example_qc_ca`() {
        val urlString = "https://gtfs.example.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - gbfs_example_qc_ca`() {
        val urlString = "https://gbfs.example.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("example.com", result)
    }

    @Test
    fun `test label from URL - gbfs_test_gbfs_example_qc_ca`() {
        val urlString = "https://api.gbfs.com"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals("gbfs.com", result)
    }

    @Test
    fun `test label from URL - dashboard_transitapp_com`() {
        val urlString = "https://dashboard.transitapp.com/"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals(null, result)
    }
}
