package org.mtransit.commons.gtfs.data

typealias AgencyId = String

data class Agency(
    val agencyId: AgencyId,
    val agencyName: Text,
    val agencyUrl: Url,
    val agencyTimezone: String,
    val agencyLang: String?,
    val agencyPhone: String?,
    val agencyFareUrl: Url?,
    val agencyEmail: String?,
    // TODO val agencyCountry: String?, // MT extension
    // TODO val agencyColor: Color?, // MT extension
)
