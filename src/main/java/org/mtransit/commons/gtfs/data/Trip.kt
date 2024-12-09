package org.mtransit.commons.gtfs.data

typealias TripId = String

typealias BlockId = String

typealias DirectionId = Int

data class Trip(
    val tripId: TripId,
    val routeId: RouteId,
    val serviceId: ServiceId,
    val tripHeadsign: Text?,
    val tripShortName: Text?,
    val directionId: DirectionId?,
    val blockId: BlockId?,
    val shapeId: ShapeId?,
    val wheelchairAccessible: Int?, // TODO Enum
    val bikesAllowed: Boolean?, // TODO Enum
)
