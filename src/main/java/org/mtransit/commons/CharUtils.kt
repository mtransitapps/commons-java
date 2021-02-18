@file:Suppress("unused")

package org.mtransit.commons

object CharUtils {

    const val EMPTY = Constants.EMPTY

    private val ROMAN_DIGITS = listOf('I', 'V', 'X') // , 'L', 'C', 'D', 'M'

    @JvmStatic
    fun countUpperCase(string: String?) = countUpperCase(string?.toCharArray())

    @JvmStatic
    fun countUpperCase(charArray: CharArray?) = charArray?.count { it.isUpperCase() } ?: 0

    @JvmStatic
    fun isDigitsOnly(str: CharSequence, notEmpty: Boolean): Boolean {
        if (str.isEmpty()) {
            return !notEmpty
        }
        return isDigitsOnly(str)
    }

    // from the Android Open Source Project by Google
    @JvmStatic
    fun isDigitsOnly(str: CharSequence): Boolean {
        val len = str.length
        for (i in 0 until len) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun isUppercaseOnly(str: CharSequence, allowWhitespace: Boolean, checkAZOnly: Boolean): Boolean {
        val len = str.length
        var aChar: Char
        for (i in 0 until len) {
            aChar = str[i]
            if (checkAZOnly && !aChar.isLetter()) {
                continue
            }
            if (aChar.isWhitespace()) {
                return if (allowWhitespace) {
                    continue
                } else {
                    false
                }
            }
            if (checkAZOnly && !aChar.isLetter()) {
                continue
            }
            if (!aChar.isUpperCase()) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun isRomanDigits(str: CharSequence): Boolean {
        val len = str.length
        var aChar: Char
        for (i in 0 until len) {
            aChar = str[i]
            if (!ROMAN_DIGITS.contains(aChar)) {
                return false
            }
        }
        return true
    }
}