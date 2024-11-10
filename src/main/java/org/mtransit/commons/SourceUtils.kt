package org.mtransit.commons

import java.net.URL

object SourceUtils {

    private val LOG_TAG: String = SourceUtils::class.java.simpleName

    @JvmStatic
    fun getSourceLabel(urlString: String) = getSourceLabel(URL(urlString))

    @JvmStatic
    fun getSourceLabel(url: URL): String? {
        try {
            var host = url.host
            host = host.removePrefix("www.")
            return host.split('.').takeLast(3).joinToString(separator = ".")
        } catch (e: Exception) {
            System.err.println("$LOG_TAG: Error while parsing source label from URL $url!")
            e.printStackTrace()
            return null
        }
    }
}