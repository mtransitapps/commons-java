package org.mtransit.commons

import java.util.Locale

object StringsCleaner {

    @JvmStatic
    fun cleanRouteLongName(originalRouteLongName: String, languages: List<Locale>?): String {
        var routeLongName = originalRouteLongName
        routeLongName = cleanString(routeLongName, languages, short = false)
        return routeLongName
    }

    @JvmStatic
    fun cleanTripHeadsign(originalTripHeadsign: String, languages: List<Locale>?): String {
        var tripHeadsign = originalTripHeadsign
        tripHeadsign = cleanString(tripHeadsign, languages, short = true)
        return tripHeadsign
    }

    @JvmStatic
    fun cleanStopName(originalStopName: String, languages: List<Locale>?): String {
        var stopName = originalStopName
        stopName = cleanString(stopName, languages, short = true)
        return stopName
    }

    private fun cleanString(originalString: String, languages: List<Locale>?, short: Boolean): String {
        var string = originalString
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
        languages?.forEach { language ->
            if (short) {
                string = CleanUtils.cleanBounds(language, string)
            }
            string = CleanUtils.cleanLabel(language, string)
        }
        return string
    }
}