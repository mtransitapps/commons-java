package org.mtransit.commons

object NumberUtils {

    @JvmStatic
    fun parseIntOrNull(string: String?): Int? = try {
        string?.toInt()
    } catch (_: NumberFormatException) {
        null
    }

    @JvmStatic
    fun parseLongOrNull(string: String?): Long? = try {
        string?.toLong()
    } catch (_: NumberFormatException) {
        null
    }
}
