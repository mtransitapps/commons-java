package org.mtransit.commons

import org.mtransit.commons.Constants.EMPTY
import java.util.regex.Pattern

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

    fun clean(input: CharSequence) = regex.replace(input, replacement) // replace ALL
}
