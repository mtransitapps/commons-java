package org.mtransit.commons

object NumberUtils {

    @JvmStatic
    fun parseIntOrNull(string: String?) = string?.toIntOrNull()

    @JvmStatic
    fun parseLongOrNull(string: String?) = string?.toLongOrNull()
}
