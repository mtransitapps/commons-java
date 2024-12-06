package org.mtransit.commons.gtfs.data

data class Direction(
    val routeId: RouteId,
    val directionId: DirectionId,
    val directionName: String,
)
