package org.mtransit.commons.gtfs.sql

val ALL_SQL_TABLES: List<TableSQL> = listOf(
    AgencySQL,
    RouteSQL,
    StopSQL,
    CalendarDateSQL,
    TripSQL,
    StopTimeSQL,
    FrequencySQL,

    DirectionSQL,
    // TODO directions (route_id, direction_id, direction, direction_destination)
    // - ca_levis: directions.txt
    // (agency_id,) route_id, direction_id, direction_name (,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color,)
    // - ca_banff: directions.txt
    // route_id,direction_id,direction
    // - ca_shawinigan: route_directions.txt
    // route_id,direction_id,route_direction_name
    // - ca  translink directions.txt
    // route_id,direction_id,direction, (route_short_name,)
    // - us juneau directions.txt
    // route_id,direction_id,direction
    // us longview river cities directions.txt
    // route_id,direction_id,direction

    // TODO later: others can wait, those ones would unlock real-time trip updates & vehicle positions
)
