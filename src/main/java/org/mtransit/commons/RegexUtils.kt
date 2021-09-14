package org.mtransit.commons

import org.intellij.lang.annotations.JdkConstants.PatternFlags
import java.util.regex.Pattern

object RegexUtils {

    var isAndroid: Boolean? = null

    @JvmField
    val DIGITS = Pattern.compile("[\\d]+")

    private const val CANON_EQ = java.util.regex.Pattern.CANON_EQ // Android: flag not supported
    private const val UNICODE_CHARACTER_CLASS = 256 // Pattern.UNICODE_CHARACTER_CLASS (Added in Android API level 24) & has no effect on Android.

    @Suppress("FunctionName")
    @PatternFlags
    @JvmStatic
    fun fCANON_EQ() = flag(CANON_EQ)

    @Suppress("FunctionName")
    @PatternFlags
    @JvmStatic
    fun fUNICODE_CHARACTER_CLASS() = flag(UNICODE_CHARACTER_CLASS)

    private fun flag(flag: Int): Int {
        return isAndroid?.let { androidPlatform ->
            if (androidPlatform) {
                0
            } else {
                flag
            }
        } ?: throw RuntimeException("Platform not set!")
    }

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