package org.mtransit.commons

@Suppress("unused")
object Constants {

    // const val DEBUG = true // DEBUG
    @JvmStatic
    val DEBUG = System.getenv("MT_DEBUG") == "mt_true" // DEBUG
    // const val DEBUG = false

    const val NEW_LINE = '\n'
    const val SPACE = ' '
    const val COLUMN_SEPARATOR = ','
    const val STRING_DELIMITER = '\''
    const val EMPTY = ""
    const val SPACE_ = " "
}
