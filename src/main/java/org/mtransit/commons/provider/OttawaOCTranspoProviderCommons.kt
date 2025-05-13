package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import org.mtransit.commons.StringUtils.EMPTY
import java.util.Locale
import java.util.regex.Pattern

object OttawaOCTranspoProviderCommons {

    private val FIX_CAIRINE_WILSON_ = CleanUtils.cleanWords("carine wilson")
    private val FIX_CAIRINE_WILSON_REPLACEMENT = CleanUtils.cleanWordsReplacement("Cairine Wilson")

    private val REMOVE_SECOND_LANGUAGE = Pattern.compile("( ~ .*$)") // FIXME i18n head-signs

    @JvmStatic
    fun cleanTripHeadsign(tripHeadSign: String, @Suppress("unused") vararg ignoreWords: String): String {
        var newTripHeadSign = tripHeadSign
        newTripHeadSign = REMOVE_SECOND_LANGUAGE.matcher(newTripHeadSign).replaceAll(EMPTY)
        newTripHeadSign = FIX_CAIRINE_WILSON_.matcher(newTripHeadSign).replaceAll(FIX_CAIRINE_WILSON_REPLACEMENT)
        newTripHeadSign = CleanUtils.fixMcXCase(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanBounds(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanSlashes(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanLabel(Locale.ENGLISH, newTripHeadSign)
        return newTripHeadSign
    }
}