package org.mtransit.commons.gtfs.sql

val ALL_SQL_TABLES: List<TableSQL> = listOf(
    AgencySQL, RouteSQL, StopSQL, CalendarDateSQL
    // TODO trip & stop_times
    // other can wait, those ones would unlock real-time trip updates & vehicle positions
)