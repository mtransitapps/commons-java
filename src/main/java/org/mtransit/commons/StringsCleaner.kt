package org.mtransit.commons

import org.jetbrains.annotations.VisibleForTesting
import java.util.Locale

object StringsCleaner {

    private const val ROUTE_LONG_NAME_SHORT_MAX_LENGTH = 33

    private val LINE_AND_SHORT_NAME = Regex(
        """(?x)
        # Alternative 1: "line <name>" at start or after space
        ( (^|\s+) line \s+ ([\p{L}\p{N}]+) )
        |
        # Alternative 2: "<name> line" at the end of the string
        (
            ^
            ( ( [\p{L}\p{N}]+ ( - | \s+(?!line\b) | ' | \.\s+(?!line\b) ) )*+ )
            ( [\p{L}\p{N}]+ ) \s+ line ( \s* $ )
        )
        """.trimIndent(),
        RegexOption.IGNORE_CASE
    )
    private const val LINE_AND_SHORT_NAME_REPLACEMENT = "$2$3$5$8"

    private val FR_LIGNE_AND_SHORT_NAME = Regex("""(^|\s+)ligne\s+([\p{L}\p{N}]+)""", RegexOption.IGNORE_CASE)
    private const val FR_LIGNE_AND_SHORT_NAME_REPLACEMENT = "$1$2"

    @JvmOverloads
    @JvmStatic
    fun cleanRouteLongName(
        originalRouteLongName: String,
        languages: List<Locale>?,
        @Suppress("unused") routeType: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        lowerUCWordsMinPct: Float? = null,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var routeLongName = originalRouteLongName
        if (languages?.contains(Locale.ENGLISH) == true) {
            routeLongName = LINE_AND_SHORT_NAME.replace(routeLongName, LINE_AND_SHORT_NAME_REPLACEMENT)
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            routeLongName = FR_LIGNE_AND_SHORT_NAME.replace(routeLongName, FR_LIGNE_AND_SHORT_NAME_REPLACEMENT)
        }
        val makeShorter = routeLongName.length > ROUTE_LONG_NAME_SHORT_MAX_LENGTH && routeLongName.contains(' ')
        routeLongName = cleanString(
            routeLongName,
            languages,
            makeShorter,
            ROUTE_LONG_NAME_SHORT_MAX_LENGTH,
            lowerUCStrings,
            lowerUCWords,
            lowerUCWordsMinPct,
            *ignoredUCWords
        )
        return routeLongName
    }

    @VisibleForTesting
    internal const val TRIP_HEADSIGN_SHORT_MAX_LENGTH = 13

    private val STATION_AND_NAME = Regex(
        """(?x)
        # Alternative 1: "station <name>" at start or after space
        ( (^|\s+) station \s+ ([\p{L}\p{N}]+) )
        |
        # Alternative 2: "<name> station" at the end of the string
        (
            ^
            ( ( [\p{L}\p{N}]+ ( - | \s+(?!station\b) | ' | \.\s+(?!station\b) ) )*+ )
            ( [\p{L}\p{N}]+ ) \s+ station ( \s* $ )
        )
        """.trimIndent(),
        RegexOption.IGNORE_CASE
    )
    private const val STATION_AND_NAME_REPLACEMENT = "$2$3$5$8"

    private val FR_STATION_AND_NAME = Regex("""(^|\s+)station\s+([\p{L}\p{N}]+)""", RegexOption.IGNORE_CASE)
    private const val FR_STATION_AND_NAME_REPLACEMENT = "$1$2"

    @JvmOverloads
    @JvmStatic
    fun cleanTripHeadsign(
        originalTripHeadsign: String,
        languages: List<Locale>?,
        routeType: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        lowerUCWordsMinPct: Float? = null,
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
                    tripHeadsign = STATION_AND_NAME.replace(tripHeadsign, STATION_AND_NAME_REPLACEMENT)
                }
            }
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            when (routeType) {
                1, // subway
                    -> {
                    tripHeadsign = FR_STATION_AND_NAME.replace(tripHeadsign, FR_STATION_AND_NAME_REPLACEMENT)
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
        tripHeadsign = cleanString(
            tripHeadsign,
            languages,
            makeShorter,
            TRIP_HEADSIGN_SHORT_MAX_LENGTH,
            lowerUCStrings,
            lowerUCWords,
            lowerUCWordsMinPct,
            *ignoredUCWords
        )
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
        lowerUCWordsMinPct: Float? = null,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var stopName = originalStopName
        if (languages?.contains(Locale.ENGLISH) == true) {
            when (routeType) {
                0, // light rail
                1, // subway
                2, // train/rail
                    -> {
                    stopName = STATION_AND_NAME.replace(stopName, STATION_AND_NAME_REPLACEMENT)
                }
            }
        }
        if (languages?.contains(Locale.FRENCH) == true) {
            when (routeType) {
                1, // subway
                    -> {
                    stopName = FR_STATION_AND_NAME.replace(stopName, FR_STATION_AND_NAME_REPLACEMENT)
                }
            }
        }
        val makeShorter = stopName.length > STOP_NAME_SHORT_MAX_LENGTH && stopName.contains(' ')
        stopName = cleanString(stopName, languages, makeShorter, STOP_NAME_SHORT_MAX_LENGTH, lowerUCStrings, lowerUCWords, lowerUCWordsMinPct, *ignoredUCWords)
        return stopName
    }

    @VisibleForTesting
    internal fun cleanString(
        originalString: String,
        languages: List<Locale>?,
        short: Boolean,
        shortMaxLength: Int,
        lowerUCStrings: Boolean = false,
        lowerUCWords: Boolean = false,
        lowerUCWordsMinPct: Float? = null,
        vararg ignoredUCWords: String = emptyArray(),
    ): String {
        var string = originalString
        languages?.forEach { language ->
            if (lowerUCWords) {
                string = CleanUtils.toLowerCaseUpperCaseWords(language, string, lowerUCWordsMinPct ?: CleanUtils.DEFAULT_MIN_UPPER_CASE_PCT, *ignoredUCWords)
            } else if (lowerUCStrings) {
                string = CleanUtils.toLowerCaseUpperCaseStrings(language, string, *ignoredUCWords)
            }
        }
        string = CleanUtils.cleanSlashes(string)
        languages?.forEach { language ->
            when (language) {
                Locale.ENGLISH -> {
                    string = CleanUtils.fixMcXCase(string)
                    string = CleanUtils.CLEAN_AT.matcher(string).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
                    string = CleanUtils.CLEAN_AND.matcher(string).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
                    if (short) {
                        string = CleanUtils.cleanStreetTypes(string)
                    }
                    string = CleanUtils.cleanNumbers(string)
                }

                Locale.FRENCH -> {
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
            }
        }
        languages?.forEach { language ->
            if (short && string.length > shortMaxLength) {
                string = CleanUtils.cleanBounds(language, string)
            }
        }
        languages?.firstOrNull()?.let { language ->
            string = CleanUtils.cleanLabel(language, string, true) // only 1st language
        }
        return string
    }
}
