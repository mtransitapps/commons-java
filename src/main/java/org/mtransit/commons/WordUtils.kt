package org.mtransit.commons

object WordUtils {

    // Apache Commons Lang
    @JvmStatic
    fun capitalize(str: String, vararg delimiters: Char): String {
        val delimLen = delimiters.size ?: -1
        if (StringUtils.isEmpty(str) || delimLen == 0) {
            return str
        }
        val buffer = str.toCharArray()
        var capitalizeNext = true
        for (i in buffer.indices) {
            val ch = buffer[i]
            if (isDelimiter(ch, delimiters)) {
                capitalizeNext = true
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch)
                capitalizeNext = false
            }
        }
        return String(buffer)
    }

    private fun isDelimiter(ch: Char, delimiters: CharArray?): Boolean {
        if (delimiters == null) {
            return Character.isWhitespace(ch)
        }
        for (delimiter in delimiters) {
            if (ch == delimiter) {
                return true
            }
        }
        return false
    }
}