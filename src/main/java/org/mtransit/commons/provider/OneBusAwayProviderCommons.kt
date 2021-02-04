package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import java.util.Locale

object OneBusAwayProviderCommons {

    @JvmStatic
    fun cleanTripHeadsign(tripHeadSign: String, vararg ignoreWords: String): String {
        var newTripHeadSign = tripHeadSign
        newTripHeadSign = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, newTripHeadSign, *ignoreWords)
        newTripHeadSign = CleanUtils.keepToAndRemoveVia(newTripHeadSign)
        newTripHeadSign = CleanUtils.CLEAN_AND.matcher(newTripHeadSign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
        newTripHeadSign = CleanUtils.CLEAN_AT.matcher(newTripHeadSign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
        newTripHeadSign = CleanUtils.cleanSlashes(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanBounds(newTripHeadSign)
        newTripHeadSign = CleanUtils.cleanStreetTypes(newTripHeadSign)
        return CleanUtils.cleanLabel(newTripHeadSign)
    }

    // OLD UNUSED CONSTANTS > TO BE DELETED
    @Deprecated(message = "Not used anymore")
    const val MORNING = 11

    @Deprecated(message = "Not used anymore")
    const val AFTERNOON = 13

    @Deprecated(message = "Not used anymore")
    const val EAST: Int = 1 // MDirectionType.EAST.intValue()

    @Deprecated(message = "Not used anymore")
    const val WEST: Int = 2 // MDirectionType.WEST.intValue()

    @Deprecated(message = "Not used anymore")
    const val NORTH: Int = 3 // MDirectionType.NORTH.intValue()

    @Deprecated(message = "Not used anymore")
    const val SOUTH: Int = 4 // MDirectionType.SOUTH.intValue()

    @Deprecated(message = "Not used anymore")
    const val EAST_SPLITTED_CIRCLE = 1000

    @Deprecated(message = "Not used anymore")
    const val WEST_SPLITTED_CIRCLE = 2000

    @Deprecated(message = "Not used anymore")
    const val NORTH_SPLITTED_CIRCLE = 3000

    @Deprecated(message = "Not used anymore")
    const val SOUTH_SPLITTED_CIRCLE = 4000
}