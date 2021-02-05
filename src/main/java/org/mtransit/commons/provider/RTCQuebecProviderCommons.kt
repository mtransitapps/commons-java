package org.mtransit.commons.provider

import org.mtransit.commons.CleanUtils
import java.util.Locale

object RTCQuebecProviderCommons {

    const val REAL_TIME_API_N = 0
    const val REAL_TIME_API_S = 1
    const val REAL_TIME_API_E = 2
    const val REAL_TIME_API_O = 3

    @JvmStatic
    fun getDirectionCode(mTripId: Long): String {
        return getDirectionCode(mTripId.toString())
    }

    @JvmStatic
    fun getDirectionCode(tripId: String): String {
        return tripId.substring(tripId.length - 1) // keep last digits
    }

    private val ANCIENNE_ = CleanUtils.cleanWordsFR("l'ancienne", "ancienne")
    private val ANCIENNE_REPLACEMENT = CleanUtils.cleanWordsReplacement("Anc")
    private val CEGER_ = CleanUtils.cleanWordsFR("cégep", "cegep")
    private val CEGERP_REPLACEMENT = CleanUtils.cleanWordsReplacement("Cgp")
    private val JACQUES_CARTIER_ = CleanUtils.cleanWordsFR("jacques-cartier")
    private val JACQUES_CARTIER_REPLACEMENT = CleanUtils.cleanWordsReplacement("J-Cartier")

    @JvmStatic
    fun cleanTripHeadsign(headsign: String): String {
        var tripHeadsign = headsign
        tripHeadsign = ANCIENNE_.matcher(tripHeadsign).replaceAll(ANCIENNE_REPLACEMENT)
        tripHeadsign = CEGER_.matcher(tripHeadsign).replaceAll(CEGERP_REPLACEMENT)
        tripHeadsign = JACQUES_CARTIER_.matcher(tripHeadsign).replaceAll(JACQUES_CARTIER_REPLACEMENT)
        tripHeadsign = CleanUtils.cleanBounds(Locale.FRENCH, tripHeadsign)
        tripHeadsign = CleanUtils.cleanStreetTypesFRCA(tripHeadsign)
        return CleanUtils.cleanLabelFR(tripHeadsign)
    }
}