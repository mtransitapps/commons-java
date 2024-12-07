package org.mtransit.commons.gtfs.data

typealias StopId = String

data class Stop(
    val stopId: StopId,
    val stopCode: String?,
    val stopName: String,
    // tts_stop_name
    // stop_desc
    val stopLat: Double,
    val stopLon: Double,
    // zone_id
    val stopUrl: Url?,
    val locationType: Int?,
    val parentStationId: StopId?,
    // stop_timezone
    val wheelchairBoarding: Int?,
    // level_id
    // platform_code
)
