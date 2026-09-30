package org.mtransit.commons

/**
 * Intended as an easy replacement from `android.text.TextUtils`
 */
object TextUtils {
    @JvmStatic
    fun isEmpty(str: CharSequence?) = str.isNullOrEmpty()

    @JvmStatic
    fun isDigitsOnly(str: CharSequence) = str.all { it.isDigit() }
}
