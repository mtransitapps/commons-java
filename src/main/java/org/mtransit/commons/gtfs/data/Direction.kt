package org.mtransit.commons.gtfs.data

// https://github.com/mbta/gtfs-documentation/blob/master/reference/gtfs.md#directionstxt
data class Direction(
    val routeId: RouteId,
    val directionId: DirectionId,
    val direction: Directions,
    val destination: Text?
)

enum class Directions {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST,
    CLOCKWISE,
    COUNTER_CLOCKWISE,
    INBOUND,
    OUTBOUND,
    LOOP,
    A_LOOP,
    B_LOOP,
}
