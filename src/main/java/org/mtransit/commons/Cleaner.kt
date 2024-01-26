package org.mtransit.commons

import org.mtransit.commons.Constants.EMPTY
import org.mtransit.commons.RegexUtils.WORD_BOUNDARY
import org.mtransit.commons.RegexUtils.group
import org.mtransit.commons.RegexUtils.groupOr

@Suppress("unused")
data class Cleaner @JvmOverloads constructor(
    val regex: Regex,
    val replacement: String = EMPTY,
) {

    @JvmOverloads
    constructor(
        regex: java.util.regex.Pattern,
        replacement: String = EMPTY,
    ) : this(
        regex = regex.toRegex(),
        replacement = replacement,
    )

    @JvmOverloads
    constructor(
        regex: String,
        replacement: String = EMPTY,
        ignoreCase: Boolean = false,
    ) : this(
        regex = regex.toRegex(options = mutableSetOf<RegexOption>().apply {
            if (ignoreCase) add(RegexOption.IGNORE_CASE)
        }),
        replacement = replacement,
    )

    constructor(
        regex: String,
        ignoreCase: Boolean,
    ) : this(
        regex = regex,
        replacement = EMPTY,
        ignoreCase = ignoreCase,
    )

    @JvmOverloads
    fun clean(input: CharSequence, replacement: String = this.replacement) = regex.replace(input, replacement) // replace ALL

    fun matcher(input: CharSequence): java.util.regex.Matcher = regex.toPattern().matcher(input)

    @JvmOverloads
    fun find(input: CharSequence, startIndex: Int = 0): Boolean = regex.find(input, startIndex) != null

    @JvmOverloads
    fun findAll(input: CharSequence, startIndex: Int = 0): Sequence<MatchResult> = regex.findAll(input, startIndex)

    @JvmOverloads
    fun findAllJ(input: CharSequence, startIndex: Int = 0) = findAll(input, startIndex).iterator()

    @JvmOverloads
    fun split(input: CharSequence, limit: Int = 0) = regex.split(input, limit)

    fun matches(input: CharSequence): Boolean = regex.matches(input)

    companion object {
        @JvmStatic
        @JvmOverloads
        fun compile(pattern: String, flags: Int = 0) = Cleaner(
            regex = pattern,
            replacement = EMPTY,
            ignoreCase = flags and java.util.regex.Pattern.CASE_INSENSITIVE == java.util.regex.Pattern.CASE_INSENSITIVE,
        )

        @JvmStatic
        fun matchWords(vararg wordsRegex: String) = group(
            group(WORD_BOUNDARY)
                    + groupOr(*wordsRegex)
                    + group(WORD_BOUNDARY)
        )
    }
}
