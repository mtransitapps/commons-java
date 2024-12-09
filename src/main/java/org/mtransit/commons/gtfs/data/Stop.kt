package org.mtransit.commons.gtfs.data

typealias StopId = String

data class Stop(
    val stopId: StopId,
    val stopCode: Text?,
    val stopName: Text,
    // val ttsStopName: Text?,
    // val stopDesc: Text?,
    val stopLat: Double,
    val stopLon: Double,
    // zone_id
    val stopUrl: Url?,
    val locationType: Int?, // TODO Enum
    val parentStationId: StopId?,
    // stop_timezone
    val wheelchairBoarding: Int?, // TODO Enum 0 1 2
    // level_id
    // val platformCode: Text?, // TODO useful!
)