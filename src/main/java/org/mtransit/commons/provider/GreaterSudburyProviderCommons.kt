package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import java.util.Locale
import java.util.regex.Pattern

object GreaterSudburyProviderCommons {

    private val FIX_SUDBURY_ = CleanUtils.cleanWord("subdury")
    private val FIX_SUDBURY_REPLACEMENT = CleanUtils.cleanWordsReplacement("Sudbury")

    private val SUDBURY_SHOPPING_CENTER_ = Pattern.compile("(sudbury shopping centre)", Pattern.CASE_INSENSITIVE)
    private const val SUDBURY_SHOPPING_CENTER_SHORT_REPLACEMENT = "Sudbury Ctr"

    private val FIX_TO_ = Pattern.compile("((\\w+)(to) )", Pattern.CASE_INSENSITIVE)
    private const val FIX_TO_REPLACEMENT = "$2 $3 "

    @JvmStatic
    fun cleanTripHeadSign(tripHeadSign: String, vararg ignoreWords: String): String {
        var newTripHeadSign = tripHeadSign
        newTripHeadSign = FIX_TO_.matcher(newTripHeadSign).replaceAll(FIX_TO_REPLACEMENT)
        newTripHeadSign = FIX_SUDBURY_.matcher(newTripHeadSign).replaceAll(FIX_SUDBURY_REPLACEMENT)
        newTripHeadSign = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, newTripHeadSign, *ignoreWords)
        newTripHeadSign = CleanUtils.keepToAndRemoveVia(newTripHeadSign)
        newTripHeadSign = SUDBURY_SHOPPING_CENTER_.matcher(newTripHeadSign).replaceAll(SUDBURY_SHOPPING_CENTER_SHORT_REPLACEMENT)
        newTripHeadSign = CleanUtils.CLEAN_AND.matcher(newTripHeadSign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
        newTripHeadSign = CleanUtils.CLEAN_AT.matcher(newTripHeadSign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
        newTripHeadSign = CleanUtils.fixMcXCase(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanNumbers(newTripHeadSign)
        // newTripHeadSign = CleanUtils.cleanBounds(newTripHeadSign) // TODO maybe later w/ conditional text shortening relative to target length
        newTripHeadSign = CleanUtils.cleanStreetTypes(newTripHeadSign)
        return CleanUtils.cleanLabel(Locale.ENGLISH, newTripHeadSign)
    }
}