package org.mtransit.commons

import java.util.Locale

object StringsCleaner {

    @JvmOverloads
    @JvmStatic
    fun cleanRouteLongName(
        originalRouteLongName: String,
        languages: List<Locale>?,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var routeLongName = originalRouteLongName
        routeLongName = cleanString(routeLongName, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = false)
        return routeLongName
    }

    @JvmOverloads
    @JvmStatic
    fun cleanTripHeadsign(
        originalTripHeadsign: String,
        languages: List<Locale>?,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
        removeVia: Boolean = false,
    ): String {
        var tripHeadsign = originalTripHeadsign
        if (languages?.contains(Locale.ENGLISH) == true) {
            tripHeadsign = if (removeVia) {
                CleanUtils.keepToAndRemoveVia(tripHeadsign)
            } else {
                CleanUtils.keepTo(tripHeadsign)
            }
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            tripHeadsign = CleanUtils.keepToFR(tripHeadsign)
        }
        tripHeadsign = cleanString(tripHeadsign, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = true)
        return tripHeadsign
    }

    @JvmOverloads
    @JvmStatic
    fun cleanStopName(
        originalStopName: String,
        languages: List<Locale>?,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var stopName = originalStopName
        stopName = cleanString(stopName, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = true)
        return stopName
    }

    private fun cleanString(
        originalString: String,
        languages: List<Locale>?,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
        short: Boolean
    ): String {
        var string = originalString
        languages?.forEach { language ->
            if (lowerUCWords) {
                string = CleanUtils.toLowerCaseUpperCaseWords(language, string, *ignoredUCWords)
            } else if (lowerUCStrings) {
                string = CleanUtils.toLowerCaseUpperCaseStrings(language, string)
            }
        }
        if (!short) {
            string = CleanUtils.cleanSlashes(string)
        }
        if (languages?.contains(Locale.ENGLISH) == true) {
            if (short) {
                string = CleanUtils.CLEAN_AND.matcher(string).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
                string = CleanUtils.CLEAN_AT.matcher(string).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
                string = CleanUtils.cleanStreetTypes(string)
                string = CleanUtils.cleanNumbers(string)
            }
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            if (short) {
                string = CleanUtils.CLEAN_ET.matcher(string).replaceAll(CleanUtils.CLEAN_ET_REPLACEMENT)
                string = CleanUtils.SAINT.matcher(string).replaceAll(CleanUtils.SAINT_REPLACEMENT)
                string = CleanUtils.cleanStreetTypesFRCA(string)
            }
        }
        languages?.forEach { language ->
            if (short) {
                string = CleanUtils.cleanBounds(language, string)
            }
            string = CleanUtils.cleanLabel(language, string)
        }
        return string
    }
}
