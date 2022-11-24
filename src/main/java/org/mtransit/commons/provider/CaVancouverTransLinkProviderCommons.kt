package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import org.mtransit.commons.Constants.EMPTY
import org.mtransit.commons.RegexUtils.BEGINNING
import org.mtransit.commons.RegexUtils.END
import org.mtransit.commons.RegexUtils.WHITESPACE_CAR
import org.mtransit.commons.RegexUtils.any
import org.mtransit.commons.RegexUtils.group
import org.mtransit.commons.RegexUtils.mGroup
import org.mtransit.commons.RegexUtils.or
import java.util.Locale
import java.util.regex.Pattern

object CaVancouverTransLinkProviderCommons {

    private val U_B_C = Pattern.compile("((^|\\s)(ubc|u b c)(\\s|$))", Pattern.CASE_INSENSITIVE)
    private val U_B_C_REPLACEMENT = mGroup(2) + "UBC" + mGroup(4)

    private val S_F_U = Pattern.compile("((^|\\s)(sfu|s f u)(\\s|$))", Pattern.CASE_INSENSITIVE)
    private val S_F_U_REPLACEMENT = mGroup(2) + "SFU" + mGroup(4)

    private val V_C_C = Pattern.compile("((^|\\s)(vcc|v c c)(\\s|$))", Pattern.CASE_INSENSITIVE)
    private val V_C_C_REPLACEMENT = mGroup(2) + "VCC" + mGroup(4)

    private val ENDS_WITH_B_LINE = Pattern.compile("((^|\\s)(- )?(b-line)(\\s|$))", Pattern.CASE_INSENSITIVE)

    @JvmField
    val CENTRAL: Pattern = Pattern.compile("((^|\\W)(central)(\\W|$))", Pattern.CASE_INSENSITIVE)

    @JvmField
    val CENTRAL_REPLACEMENT = mGroup(2) + "Ctrl" + mGroup(4)

    @JvmField
    val STATION: Pattern = Pattern.compile("((^|\\W)(stn|sta|station)(\\W|$))", Pattern.CASE_INSENSITIVE)

    @JvmField
    val STATION_REPLACEMENT = mGroup(2) + "Sta" + mGroup(4) // see @CleanUtils

    private const val PORT_COQUITLAM_SHORT = "PoCo"
    private val PORT_COQUITLAM = Pattern.compile("((^|\\W)(port coquitlam|poco)(\\W|$))", Pattern.CASE_INSENSITIVE)
    private val PORT_COQUITLAM_REPLACEMENT = mGroup(2) + PORT_COQUITLAM_SHORT + mGroup(4)

    private const val COQUITLAM_SHORT = "Coq"
    private val COQUITLAM = Pattern.compile("((^|\\W)(coquitlam|coq)(\\W|$))", Pattern.CASE_INSENSITIVE)
    private val COQUITLAM_REPLACEMENT = mGroup(2) + COQUITLAM_SHORT + mGroup(4)

    private const val PORT_SHORT = "Pt" // like GTFS & real-time API
    private val PORT = Pattern.compile("((^|\\W)(port)(\\W|$))", Pattern.CASE_INSENSITIVE)
    private val PORT_REPLACEMENT = mGroup(2) + PORT_SHORT + mGroup(4)

    private const val SURREY_SHORT = "Sry"
    private val SURREY_ = Pattern.compile("((^|\\s)(surrey)(\\s|$))", Pattern.CASE_INSENSITIVE)
    private val SURREY_REPLACEMENT = mGroup(2) + SURREY_SHORT + mGroup(4)

    @JvmField
    val REMOVE_DASH_START_END: Pattern = Pattern.compile(
        group(
            or(
                BEGINNING + any(group(WHITESPACE_CAR)) + "-" + any(group(WHITESPACE_CAR)),
                any(group(WHITESPACE_CAR)) + "-" + any(group(WHITESPACE_CAR)) + END
            )
        ),
        Pattern.CASE_INSENSITIVE
    )

    @JvmStatic
    fun getIgnoredWords(): Array<String> {
        return arrayOf(
            "FS", "NS",
            "AM", "PM",
            "SW", "NW", "SE", "NE",
            "UBC", "SFU", "VCC"
        )
    }

    @JvmStatic
    fun cleanTripHeadsign(oldTripHeadsign: String): String {
        var tripHeadsign = oldTripHeadsign
        tripHeadsign = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, tripHeadsign, *getIgnoredWords())
        tripHeadsign = CleanUtils.keepTo(tripHeadsign) // keep via or not?
        tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign)

        tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT)
        tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT)
        tripHeadsign = CENTRAL.matcher(tripHeadsign).replaceAll(CENTRAL_REPLACEMENT)
        tripHeadsign = STATION.matcher(tripHeadsign).replaceAll(STATION_REPLACEMENT)

        tripHeadsign = S_F_U.matcher(tripHeadsign).replaceAll(S_F_U_REPLACEMENT)
        tripHeadsign = U_B_C.matcher(tripHeadsign).replaceAll(U_B_C_REPLACEMENT)
        tripHeadsign = V_C_C.matcher(tripHeadsign).replaceAll(V_C_C_REPLACEMENT)
        tripHeadsign = SURREY_.matcher(tripHeadsign).replaceAll(SURREY_REPLACEMENT)
        tripHeadsign = PORT_COQUITLAM.matcher(tripHeadsign).replaceAll(PORT_COQUITLAM_REPLACEMENT)
        tripHeadsign = COQUITLAM.matcher(tripHeadsign).replaceAll(COQUITLAM_REPLACEMENT)
        tripHeadsign = PORT.matcher(tripHeadsign).replaceAll(PORT_REPLACEMENT)
        tripHeadsign = ENDS_WITH_B_LINE.matcher(tripHeadsign).replaceAll(EMPTY)

        tripHeadsign = REMOVE_DASH_START_END.matcher(tripHeadsign).replaceAll(EMPTY)

        tripHeadsign = CleanUtils.fixMcXCase(tripHeadsign)
        tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign)
        tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign)
        tripHeadsign = CleanUtils.cleanLabel(tripHeadsign)
        return tripHeadsign
    }
}