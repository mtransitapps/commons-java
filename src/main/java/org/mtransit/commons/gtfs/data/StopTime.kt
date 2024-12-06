package org.mtransit.commons.gtfs.data

data class StopTime(
    val tripId: TripId,
    val stopId: StopId,
    val stopSequence: Int,
    val arrivalTime: String, // HH:MM:SS or H:MM:SS
    val departureTime: String, // HH:MM:SS or H:MM:SS
    val stopHeadsign: String?,
    val pickupType: Int?, // TODO Enum
    val dropOffType: Int?, // TODO Enum
    val timePoint: Int?, // TODO Enum
)