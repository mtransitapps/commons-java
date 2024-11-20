package org.mtransit.commons

import java.net.URL

object SourceUtils {

    private val LOG_TAG: String = SourceUtils::class.java.simpleName

    private val BLACK_LIST = listOf(
        "api",
        "assets",
        "data",
        "gbfs",
        "gtfs",
        "opendata",
        "www",
    )

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
            return url?.host?.split('.')?.takeLast(3)
                ?.dropWhile(minSize = 2) { BLACK_LIST.contains(it) } // drop 1st elements if in black list
                ?.joinToString(separator = ".")
        } catch (e: Exception) {
            System.err.println("$LOG_TAG: Error while parsing source label from URL $url!")
            e.printStackTrace()
            return null
        }
    }
}