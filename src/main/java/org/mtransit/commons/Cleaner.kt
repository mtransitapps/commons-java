package org.mtransit.commons

import org.mtransit.commons.Constants.EMPTY
import java.util.regex.Pattern

@Suppress("unused")
data class Cleaner @JvmOverloads constructor(
    val regex: Regex,
    val replacement: String = EMPTY,
) {

    @JvmOverloads
    constructor(
        regex: Pattern,
        replacement: String = EMPTY,
    ) : this(
        regex.toRegex(),
        replacement,
    )

    @JvmOverloads
    constructor(
        regex: String,
        replacement: String = EMPTY,
        ignoreCase: Boolean = false,
    ) : this(
        regex.toRegex(options = mutableSetOf<RegexOption>().apply {
            if (ignoreCase) add(RegexOption.IGNORE_CASE)
        }),
        replacement,
    )

    @JvmOverloads
    fun clean(input: CharSequence, replacement: String = this.replacement) = regex.replace(input, replacement) // replace ALL

    @JvmOverloads
    fun find(input: CharSequence, startIndex: Int = 0): Boolean = regex.find(input, startIndex) != null

    fun matches(input: CharSequence): Boolean = regex.matches(input)
}
