package org.mtransit.commons.gtfs.data

typealias RouteId = String

data class Route(
    val routeId: RouteId,
    val originalRouteId: RouteId,
    val agencyId: AgencyId, // Optional (or empty)
    val routeShortName: Text,
    val routeLongName: Text?,
    val routeDesc: Text?,
    val routeType: Int, // TODO enum
    val routeUrl: Url?,
    val routeColor: Color?,
    val routeTextColor: Color?,
    val routeSortOrder: Int?,
)
