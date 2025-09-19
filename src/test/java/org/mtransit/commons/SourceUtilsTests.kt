package org.mtransit.commons

import org.junit.Assert.assertEquals
import org.junit.Test

class SourceUtilsTests {

    companion object {
        private const val INVALID_URL = "" // TODO null? // UI not compatible with null source label (2025-03-12)
    }

    @Test
    fun `test label from URL - null`() {
        assertEquals(INVALID_URL, SourceUtils.getSourceLabel(urlString = ""))

        assertEquals(INVALID_URL, SourceUtils.getSourceLabel(urlString = "invalid URL"))
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
    fun `test label from URL - open_data_example_qc_ca`() {
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
    fun `test label from URL - cdn_example_qc_ca`() {
        val urlString = "https://cdn.example.com"

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
    fun `test label from URL - azure_fd_net`() {
        val urlString = "https://oct-gtfs-emasagcnfmcgeham.z01.azurefd.net"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals(INVALID_URL, result) // UI not compatible with null source label (2025-03-12)
    }

    @Test
    fun `test label from URL - azure_api_net`() {
        val urlString = "https://nextrip-public-api.azure-api.net/"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals(INVALID_URL, result) // UI not compatible with null source label (2025-03-12)
    }

    @Test
    fun `test label from URL - dashboard_transit_app_com`() {
        val urlString = "https://dashboard.transitapp.com/"

        val result = SourceUtils.getSourceLabel(urlString)

        assertEquals(INVALID_URL, result) // UI not compatible with null source label (2025-03-12)
    }
}
