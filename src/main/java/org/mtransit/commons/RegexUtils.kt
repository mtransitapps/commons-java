package org.mtransit.commons

import java.util.regex.Pattern

object RegexUtils {

    const val UNICODE_CHARACTER_CLASS = 256; // java.util.regex.Pattern.UNICODE_CHARACTER_CLASS (Added in Android API level 24)

    @JvmStatic
    fun replaceAll(string: String?, patterns: Array<Pattern>?, replacement: String): String? {
        if (string == null) {
            return null
        }
        return if (patterns == null) {
            string
        } else replaceAllNN(string, patterns, replacement)
    }

    @JvmStatic
    fun replaceAllNN(string: String, patterns: Array<Pattern>, replacement: String): String {
        var newString = string
        if (newString.isEmpty()) {
            return newString
        }
        for (pattern in patterns) {
            newString = pattern.matcher(newString).replaceAll(replacement)
        }
        return newString
    }
}