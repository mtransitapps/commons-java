package org.mtransit.commons.gtfs.data

data class Frequency(
    val tripId: TripId,
    val startTime: Time?,
    val endTime: Time?,
    val headwaySecs: Int,
    val exactTimes: Int?,
)
