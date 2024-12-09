package org.mtransit.commons.gtfs.data

data class StopTime(
    val tripId: TripId,
    val stopId: StopId,
    val stopSequence: Int,
    val arrivalTime: Time,
    val departureTime: Time,
    val stopHeadsign: Text?,
    val pickupType: Int?, // TODO Enum
    val dropOffType: Int?, // TODO Enum
    val timePoint: Int?, // TODO Enum
)