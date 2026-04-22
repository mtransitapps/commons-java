package org.mtransit.commons

import org.mtransit.commons.StringUtils.EMPTY
import java.util.Locale

object StringsCleaner {

    private const val ROUTE_LONG_NAME_SHORT_MAX_LENGTH = 33

    @JvmOverloads
    @JvmStatic
    fun cleanRouteLongName(
        originalRouteLongName: String,
        languages: List<Locale>?,
        @Suppress("unused") routeType: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var routeLongName = originalRouteLongName
        if (languages?.contains(Locale.ENGLISH) == true) {
            routeLongName = CleanUtils.LINE.matcher(routeLongName).replaceAll(CleanUtils.LINE_REPLACEMENT)
        }
        val makeShorter = routeLongName.length > ROUTE_LONG_NAME_SHORT_MAX_LENGTH && routeLongName.contains(' ')
        routeLongName = cleanString(routeLongName, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = makeShorter, shortMaxLength = ROUTE_LONG_NAME_SHORT_MAX_LENGTH)
        return routeLongName
    }

    private const val TRIP_HEADSIGN_SHORT_MAX_LENGTH = 13

    @JvmOverloads
    @JvmStatic
    fun cleanTripHeadsign(
        originalTripHeadsign: String,
        languages: List<Locale>?,
        routeType: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
        removeVia: Boolean = false,
    ): String {
        var tripHeadsign = originalTripHeadsign
        if (languages?.contains(Locale.ENGLISH) == true) {
            when (routeType) {
                0, // light rail
                1, // subway
                2, // train/rail
                    -> {
                    tripHeadsign = CleanUtils.STATION.matcher(tripHeadsign).replaceAll(EMPTY)
                }
            }
        }
        if (languages?.contains(Locale.ENGLISH) == true) {
            tripHeadsign = if (removeVia) {
                CleanUtils.keepToAndRemoveVia(tripHeadsign)
            } else {
                CleanUtils.keepTo(tripHeadsign)
            }
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            tripHeadsign = CleanUtils.keepToFR(tripHeadsign)
            if (removeVia) {
                tripHeadsign = CleanUtils.removeVia(tripHeadsign)
            }
        }
        val makeShorter = tripHeadsign.length > TRIP_HEADSIGN_SHORT_MAX_LENGTH && tripHeadsign.contains(' ')
        tripHeadsign = cleanString(tripHeadsign, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = makeShorter, shortMaxLength = TRIP_HEADSIGN_SHORT_MAX_LENGTH)
        if (tripHeadsign.length > TRIP_HEADSIGN_SHORT_MAX_LENGTH) {
            tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign, true)
        }
        return tripHeadsign
    }

    private const val STOP_NAME_SHORT_MAX_LENGTH = 27 // almost as much space as route long name

    @JvmOverloads
    @JvmStatic
    fun cleanStopName(
        originalStopName: String,
        languages: List<Locale>?,
        routeType: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var stopName = originalStopName
        if (languages?.contains(Locale.ENGLISH) == true) {
            when (routeType) {
                0, // light rail
                1, // subway
                2, // train/rail
                    -> {
                    stopName = CleanUtils.STATION.matcher(stopName).replaceAll(EMPTY)
                }
            }

        }
        val makeShorter = stopName.length > STOP_NAME_SHORT_MAX_LENGTH && stopName.contains(' ')
        stopName = cleanString(stopName, languages, lowerUCStrings, lowerUCWords, *ignoredUCWords, short = makeShorter, shortMaxLength = STOP_NAME_SHORT_MAX_LENGTH)
        return stopName
    }

    private fun cleanString(
        originalString: String,
        languages: List<Locale>?,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        vararg ignoredUCWords: String = emptyArray(),
        short: Boolean,
        shortMaxLength: Int,
    ): String {
        var string = originalString
        languages?.forEach { language ->
            if (lowerUCWords) {
                string = CleanUtils.toLowerCaseUpperCaseWords(language, string, *ignoredUCWords)
            } else if (lowerUCStrings) {
                string = CleanUtils.toLowerCaseUpperCaseStrings(language, string, *ignoredUCWords)
            }
        }
        string = CleanUtils.cleanSlashes(string)
        if (languages?.contains(Locale.ENGLISH) == true) {
            string = CleanUtils.fixMcXCase(string)
            string = CleanUtils.CLEAN_AT.matcher(string).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
            if (short) {
                string = CleanUtils.CLEAN_AND.matcher(string).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
                string = CleanUtils.cleanStreetTypes(string)
            }
            string = CleanUtils.cleanNumbers(string)
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            if (short) {
                string = CleanUtils.CLEAN_ET.matcher(string).replaceAll(CleanUtils.CLEAN_ET_REPLACEMENT)
                string = CleanUtils.SAINT.matcher(string).replaceAll(CleanUtils.SAINT_REPLACEMENT)
                string = CleanUtils.cleanStreetTypesFRCA(string)
                string = CleanUtils.removePointsI(string) // BEFORE next regexes
                string = CleanUtils.ALL_FACE_A_REGEX.replace(string, CleanUtils.ALL_FACE_A_REGEX_REPLACEMENT)
                string = CleanUtils.ALL_ST_REGEX.replace(string, CleanUtils.ALL_ST_REGEX_REPLACEMENT)
                string = CleanUtils.ALL_CHARS_REGEX.replace(string, CleanUtils.ALL_CHARS_REGEX_REPLACEMENT)
            }
        }
        languages?.forEach { language ->
            if (short && string.length > shortMaxLength) {
                string = CleanUtils.cleanBounds(language, string)
            }
            string = CleanUtils.cleanLabel(language, string)
        }
        return string
    }
}
