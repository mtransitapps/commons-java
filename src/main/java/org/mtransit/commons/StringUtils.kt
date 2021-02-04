@file:Suppress("unused")

package org.mtransit.commons

object StringUtils {

    const val EMPTY = Constants.EMPTY

    @JvmStatic
    fun isEmpty(string: CharSequence?) = string.isNullOrEmpty()

    @JvmStatic
    fun isBlank(string: CharSequence?) = string.isNullOrBlank()

    @JvmStatic
    fun equals(string1: CharSequence?, string2: CharSequence?) = string1 == string2

    @JvmStatic
    fun isNumeric(string: CharSequence) = CharUtils.isDigitsOnly(string)
}