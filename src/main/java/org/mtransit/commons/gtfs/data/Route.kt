package org.mtransit.commons.gtfs.data

typealias RouteId = String

data class Route(
    val routeId: RouteId,
    val agencyId: AgencyId, // Optional (or empty)
    val routeShortName: String,
    val routeLongName: String?,
    val routeDesc: String?,
    val routeType: Int,
    val routeUrl: String?,
    val routeColor: Color?,
    val routeTextColor: Color?,
    val routeSortOrder: Int?,
)
