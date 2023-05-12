package org.mtransit.commons

import java.util.regex.Pattern

// https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
@Suppress("unused", "MemberVisibilityCanBePrivate")
object RegexUtils {

    const val ANY = "."

    const val WHITESPACE_CAR = "\\s"
    const val NON_WHITESPACE_CAR = "\\S"

    const val DIGIT_CAR = "\\d"
    const val NON_DIGIT_CAR = "\\D"

    const val WORD_CAR = "\\w" // [a-zA-Z_0-9]
    const val ALPHA_NUM_CAR = WORD_CAR
    const val NON_WORD_CAR = "\\W"

    const val BEGINNING = "^"
    const val END = "$"

    @JvmField
    val DIGITS: Pattern = Pattern.compile(atLeastOne(DIGIT_CAR))

    private const val CANON_EQ = java.util.regex.Pattern.CANON_EQ // Android: flag not supported
    private const val UNICODE_CHARACTER_CLASS = 256 // Pattern.UNICODE_CHARACTER_CLASS (Added in Android API level 24) & has no effect on Android.

    @Suppress("FunctionName")
    @JvmStatic
    fun fCANON_EQ() = flag(CANON_EQ)

    @Suppress("FunctionName")
    @JvmStatic
    fun fUNICODE_CHARACTER_CLASS() = flag(UNICODE_CHARACTER_CLASS)

    private fun flag(flag: Int): Int {
        return CommonsApp.isAndroid?.let { androidPlatform ->
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

    @JvmStatic
    fun group(string: String) = "($string)"

    @JvmStatic
    fun matchGroup(g: Int) = "$$g"

    @JvmStatic
    fun mGroup(g: Int) = matchGroup(g)

    @JvmStatic
    fun except(string: String) = "[^$string]"

    @JvmStatic
    fun or(vararg strings: String): String = strings.joinToString(separator = "|") { it }

    @JvmStatic
    fun onceOrNot(string: String) = "$string?"

    @JvmStatic
    fun maybe(string: String) = onceOrNot(string)

    @JvmStatic
    fun zeroOrMore(string: String) = "$string*"

    @JvmStatic
    fun any(string: String) = zeroOrMore(string)

    @Deprecated(message = "Use any() instead.", replaceWith = ReplaceWith("any(string)", "org.mtransit.commons.RegexUtils.any"))
    @JvmStatic
    fun many(string: String) = zeroOrMore(string)

    @JvmStatic
    fun oneOrMore(string: String) = "$string+"

    @JvmStatic
    fun atLeastOne(string: String) = oneOrMore(string)

    @JvmStatic
    fun exactly(string: String, times: Int) = "$string{$times}"

    @JvmStatic
    fun atLeast(string: String, atLeast: Int) = "$string{$atLeast,}"

    @JvmStatic
    fun atLeast(string: String, atLeast: Int, notMore: Int) = "$string{$atLeast,$notMore}"
}