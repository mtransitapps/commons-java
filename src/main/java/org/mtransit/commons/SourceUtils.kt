package org.mtransit.commons

import java.net.URL

object SourceUtils {

    private val LOG_TAG: String = SourceUtils::class.java.simpleName

    @JvmStatic
    fun getSourceLabel(urlString: String?) = urlString?.let {
        getSourceLabel(
            @Suppress("DEPRECATION") // since Java 20
            URL(it)
        )
    }

    @JvmStatic
    fun getSourceLabel(url: URL?): String? {
        try {
            return url?.host?.removePrefix("www.")?.split('.')?.takeLast(3)?.joinToString(separator = ".")
        } catch (e: Exception) {
            System.err.println("$LOG_TAG: Error while parsing source label from URL $url!")
            e.printStackTrace()
            return null
        }
    }
}