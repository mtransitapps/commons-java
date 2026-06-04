package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import java.util.Locale

object OttawaOCTranspoProviderCommons {

    private val FIX_CAIRINE_WILSON_ = CleanUtils.cleanWords("carine wilson")
    private val FIX_CAIRINE_WILSON_REPLACEMENT = CleanUtils.cleanWordsReplacement("Cairine Wilson")

    private val REMOVE_SECOND_LANGUAGE = """(?U)(\s+~\s+[^<>]+?)(?=\s*<>|$)""".toRegex(RegexOption.IGNORE_CASE) // FIXME i18n head-signs
    private const val REMOVE_SECOND_LANGUAGE_REPLACEMENT = "$2$3$5"

    @JvmStatic
    fun cleanTripHeadsign(tripHeadSign: String, @Suppress("unused") vararg ignoreWords: String): String {
        var newTripHeadSign = tripHeadSign
        newTripHeadSign = REMOVE_SECOND_LANGUAGE.replace(newTripHeadSign, REMOVE_SECOND_LANGUAGE_REPLACEMENT)
        newTripHeadSign = FIX_CAIRINE_WILSON_.matcher(newTripHeadSign).replaceAll(FIX_CAIRINE_WILSON_REPLACEMENT)
        newTripHeadSign = CleanUtils.fixMcXCase(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanBounds(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanSlashes(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanLabel(Locale.ENGLISH, newTripHeadSign)
        return newTripHeadSign
    }
}
