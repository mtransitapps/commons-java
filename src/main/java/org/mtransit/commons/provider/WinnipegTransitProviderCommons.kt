package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import java.util.regex.Pattern

object WinnipegTransitProviderCommons {

    private val FIX_POINT_SPACE_ = Pattern.compile("((^|\\S)(\\.)(\\S|$))", Pattern.CASE_INSENSITIVE)
    private const val FIX_POINT_SPACE_REPLACEMENT = "$2$3 $4" // "st.abc" -> "st. abc"

    @JvmStatic
    fun cleanTripHeadsign(tripHeadSign: String, @Suppress("unused") vararg ignoreWords: String): String {
        var newTripHeadSign = tripHeadSign
        newTripHeadSign = CleanUtils.CLEAN_AND.matcher(newTripHeadSign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
        newTripHeadSign = FIX_POINT_SPACE_.matcher(newTripHeadSign).replaceAll(FIX_POINT_SPACE_REPLACEMENT)
        newTripHeadSign = CleanUtils.keepToAndRemoveVia(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanBounds(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanStreetTypes(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanNumbers(newTripHeadSign)
        return CleanUtils.cleanLabel(newTripHeadSign)
    }
}