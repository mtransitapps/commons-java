package org.mtransit.commons

import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.getSQLDropIfExistsQuery

@Suppress("MemberVisibilityCanBePrivate")
object GTFSCommons {

    const val DB_FILE_NAME = "gtfs_rts_db" // no extension in Android resources

    @JvmStatic
    fun getDBFileName(fileBase: String) = "$fileBase$DB_FILE_NAME"

    const val T_ROUTE = "route"
    const val T_ROUTE_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_ROUTE_K_SHORT_NAME = "short_name"
    const val T_ROUTE_K_LONG_NAME = "long_name"
    const val T_ROUTE_K_COLOR = "color"

    @JvmStatic
    var T_ROUTE_SQL_CREATE = SQLCreateBuilder.getNew(T_ROUTE)
        .appendColumn(T_ROUTE_K_ID, SQLUtils.INT_PK)
        .appendColumn(T_ROUTE_K_SHORT_NAME, SQLUtils.TXT)
        .appendColumn(T_ROUTE_K_LONG_NAME, SQLUtils.TXT)
        .appendColumn(T_ROUTE_K_COLOR, SQLUtils.TXT)
        .build()

    @JvmStatic
    val T_ROUTE_SQL_INSERT = SQLInsertBuilder.getNew(T_ROUTE)
        .appendColumn(T_ROUTE_K_ID)
        .appendColumn(T_ROUTE_K_SHORT_NAME)
        .appendColumn(T_ROUTE_K_LONG_NAME)
        .appendColumn(T_ROUTE_K_COLOR)
        .build()

    @JvmStatic
    val T_ROUTE_SQL_DROP = getSQLDropIfExistsQuery(T_ROUTE)

    const val T_TRIP = "trip"
    const val T_TRIP_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_TRIP_K_HEADSIGN_TYPE = "headsign_type"
    const val T_TRIP_K_HEADSIGN_VALUE = "headsign_value" // really?
    const val T_TRIP_K_ROUTE_ID = "route_id"

    @JvmStatic
    val T_TRIP_SQL_CREATE = SQLCreateBuilder.getNew(T_TRIP)
        .appendColumn(T_TRIP_K_ID, SQLUtils.INT_PK)
        .appendColumn(T_TRIP_K_HEADSIGN_TYPE, SQLUtils.INT)
        .appendColumn(T_TRIP_K_HEADSIGN_VALUE, SQLUtils.TXT)
        .appendColumn(T_TRIP_K_ROUTE_ID, SQLUtils.INT)
        .appendForeignKey(T_TRIP_K_ROUTE_ID, T_ROUTE, T_ROUTE_K_ID)
        .build()

    @JvmStatic
    val T_TRIP_SQL_INSERT = SQLInsertBuilder.getNew(T_TRIP)
        .appendColumn(T_TRIP_K_ID)
        .appendColumn(T_TRIP_K_HEADSIGN_TYPE)
        .appendColumn(T_TRIP_K_HEADSIGN_VALUE)
        .appendColumn(T_TRIP_K_ROUTE_ID)
        .build()

    @JvmStatic
    val T_TRIP_SQL_DROP = getSQLDropIfExistsQuery(T_TRIP)

    const val T_STOP = "stop"
    const val T_STOP_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_STOP_K_CODE = "code" // optional
    const val T_STOP_K_NAME = "name"
    const val T_STOP_K_LAT = "lat"
    const val T_STOP_K_LNG = "lng"

    @JvmStatic
    var T_STOP_SQL_CREATE = SQLCreateBuilder.getNew(T_STOP)
        .appendColumn(T_STOP_K_ID, SQLUtils.INT_PK)
        .appendColumn(T_STOP_K_CODE, SQLUtils.TXT)
        .appendColumn(T_STOP_K_NAME, SQLUtils.TXT)
        .appendColumn(T_STOP_K_LAT, SQLUtils.REAL)
        .appendColumn(T_STOP_K_LNG, SQLUtils.REAL)
        .build()

    @JvmStatic
    var T_STOP_SQL_INSERT = SQLInsertBuilder.getNew(T_STOP)
        .appendColumn(T_STOP_K_ID)
        .appendColumn(T_STOP_K_CODE)
        .appendColumn(T_STOP_K_NAME)
        .appendColumn(T_STOP_K_LAT)
        .appendColumn(T_STOP_K_LNG)
        .build()

    @JvmStatic
    val T_STOP_SQL_DROP = getSQLDropIfExistsQuery(T_STOP)

    const val T_TRIP_STOPS = "trip_stops"
    const val T_TRIP_STOPS_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_TRIP_STOPS_K_TRIP_ID = "trip_id"
    const val T_TRIP_STOPS_K_STOP_ID = "stop_id"
    const val T_TRIP_STOPS_K_STOP_SEQUENCE = "stop_sequence"
    const val T_TRIP_STOPS_K_DESCENT_ONLY = "decent_only"

    @JvmStatic
    val T_TRIP_STOPS_SQL_CREATE = SQLCreateBuilder.getNew(T_TRIP_STOPS)
        .appendColumn(T_TRIP_STOPS_K_ID, SQLUtils.INT_PK_AUTO)
        .appendColumn(T_TRIP_STOPS_K_TRIP_ID, SQLUtils.INT)
        .appendColumn(T_TRIP_STOPS_K_STOP_ID, SQLUtils.INT)
        .appendColumn(T_TRIP_STOPS_K_STOP_SEQUENCE, SQLUtils.INT)
        .appendColumn(T_TRIP_STOPS_K_DESCENT_ONLY, SQLUtils.INT)
        .appendForeignKey(T_TRIP_STOPS_K_TRIP_ID, T_TRIP, T_TRIP_K_ID)
        .appendForeignKey(T_TRIP_STOPS_K_STOP_ID, T_STOP, T_STOP_K_ID)
        .build()

    @JvmStatic
    val T_TRIP_STOPS_SQL_INSERT = SQLInsertBuilder.getNew(T_TRIP_STOPS)
        .appendColumn(T_TRIP_STOPS_K_TRIP_ID)
        .appendColumn(T_TRIP_STOPS_K_STOP_ID)
        .appendColumn(T_TRIP_STOPS_K_STOP_SEQUENCE)
        .appendColumn(T_TRIP_STOPS_K_DESCENT_ONLY)
        .build()

    @JvmStatic
    val T_TRIP_STOPS_SQL_DROP = getSQLDropIfExistsQuery(T_TRIP_STOPS)

    const val T_SERVICE_DATES = "service_dates"
    const val T_SERVICE_DATES_K_SERVICE_ID = "service_id"
    const val T_SERVICE_DATES_K_DATE = "date"

    @JvmStatic
    val T_SERVICE_DATES_SQL_CREATE = SQLCreateBuilder.getNew(T_SERVICE_DATES) //
        .appendColumn(T_SERVICE_DATES_K_SERVICE_ID, SQLUtils.TXT) //
        .appendColumn(T_SERVICE_DATES_K_DATE, SQLUtils.INT) //
        .build()

    @JvmStatic
    val T_SERVICE_DATES_SQL_INSERT = SQLInsertBuilder.getNew(T_SERVICE_DATES) //
        .appendColumn(T_SERVICE_DATES_K_SERVICE_ID).appendColumn(T_SERVICE_DATES_K_DATE).build()

    @JvmStatic
    val T_SERVICE_DATES_SQL_DROP = getSQLDropIfExistsQuery(T_SERVICE_DATES)
}