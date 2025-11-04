package org.mtransit.commons

import org.mtransit.commons.Constants.EMPTY
import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import java.util.regex.Pattern

@Suppress("MemberVisibilityCanBePrivate", "unused")
object GTFSCommons {

    const val DB_FILE_NAME = "gtfs_rds_db" // no extension in Android resources

    @JvmStatic
    fun getDBFileName(fileBase: String) = "$fileBase$DB_FILE_NAME"

    // region Route

    const val T_ROUTE = "route"
    const val T_ROUTE_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_ROUTE_K_SHORT_NAME = "short_name"
    const val T_ROUTE_K_LONG_NAME = "long_name"
    const val T_ROUTE_K_COLOR = "color"
    const val T_ROUTE_K_ORIGINAL_ID_HASH = "o_id_hash"
    const val T_ROUTE_K_TYPE = "type"

    @JvmStatic
    val T_ROUTE_SQL_CREATE = SQLCreateBuilder.getNew(T_ROUTE).apply {
        appendColumn(T_ROUTE_K_ID, SQLUtils.INT_PK)
        appendColumn(T_ROUTE_K_SHORT_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_LONG_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_COLOR, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ORIGINAL_ID_HASH, SQLUtils.INT)
        if (FeatureFlags.F_EXPORT_ORIGINAL_ROUTE_TYPE) {
            appendColumn(T_ROUTE_K_TYPE, SQLUtils.INT)
        }
    }.build()

    @JvmStatic
    val T_ROUTE_SQL_INSERT = SQLInsertBuilder.getNew(T_ROUTE).apply {
        appendColumn(T_ROUTE_K_ID)
        appendColumn(T_ROUTE_K_SHORT_NAME)
        appendColumn(T_ROUTE_K_LONG_NAME)
        appendColumn(T_ROUTE_K_COLOR)
        appendColumn(T_ROUTE_K_ORIGINAL_ID_HASH)
        if (FeatureFlags.F_EXPORT_ORIGINAL_ROUTE_TYPE) {
            appendColumn(T_ROUTE_K_TYPE)
        }
    }.build()

    @JvmStatic
    val T_ROUTE_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_ROUTE)

    // endregion Route

    // region Direction

    const val T_DIRECTION = "trip" // do not change to avoid breaking compat w/ old modules
    const val T_DIRECTION_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_DIRECTION_K_HEADSIGN_TYPE = "headsign_type"
    const val T_DIRECTION_K_HEADSIGN_VALUE = "headsign_value" // really?
    const val T_DIRECTION_K_ROUTE_ID = "route_id"

    @Deprecated("use T_DIRECTION instead", ReplaceWith("T_DIRECTION"))
    const val T_TRIP = T_DIRECTION
    @Deprecated("use T_DIRECTION_K_ID instead", ReplaceWith("T_DIRECTION_K_ID"))
    const val T_TRIP_K_ID = T_DIRECTION_K_ID
    @Deprecated("use T_DIRECTION_K_HEADSIGN_TYPE instead", ReplaceWith("T_DIRECTION_K_HEADSIGN_TYPE"))
    const val T_TRIP_K_HEADSIGN_TYPE = T_DIRECTION_K_HEADSIGN_TYPE
    @Deprecated("use T_DIRECTION_K_HEADSIGN_VALUE instead", ReplaceWith("T_DIRECTION_K_HEADSIGN_VALUE"))
    const val T_TRIP_K_HEADSIGN_VALUE = T_DIRECTION_K_HEADSIGN_VALUE
    @Deprecated("use T_DIRECTION_K_ROUTE_ID instead", ReplaceWith("T_DIRECTION_K_ROUTE_ID"))
    const val T_TRIP_K_ROUTE_ID = T_DIRECTION_K_ROUTE_ID

    @JvmStatic
    val T_DIRECTION_SQL_CREATE = SQLCreateBuilder.getNew(T_DIRECTION).apply {
        appendColumn(T_DIRECTION_K_ID, SQLUtils.INT_PK)
        appendColumn(T_DIRECTION_K_HEADSIGN_TYPE, SQLUtils.INT)
        appendColumn(T_DIRECTION_K_HEADSIGN_VALUE, SQLUtils.TXT)
        appendColumn(T_DIRECTION_K_ROUTE_ID, SQLUtils.INT)
        appendForeignKey(T_DIRECTION_K_ROUTE_ID, T_ROUTE, T_ROUTE_K_ID)
    }.build()

    @Deprecated("use T_DIRECTION_SQL_CREATE instead", ReplaceWith("T_DIRECTION_SQL_CREATE"))
    @JvmStatic
    val T_TRIP_SQL_CREATE = T_DIRECTION_SQL_CREATE

    @JvmStatic
    val T_DIRECTION_SQL_INSERT = SQLInsertBuilder.getNew(T_DIRECTION).apply {
        appendColumn(T_DIRECTION_K_ID)
        appendColumn(T_DIRECTION_K_HEADSIGN_TYPE)
        appendColumn(T_DIRECTION_K_HEADSIGN_VALUE)
        appendColumn(T_DIRECTION_K_ROUTE_ID)
    }.build()

    @Deprecated("use T_DIRECTION_SQL_INSERT instead", ReplaceWith("T_DIRECTION_SQL_INSERT"))
    @JvmStatic
    val T_TRIP_SQL_INSERT = T_DIRECTION_SQL_INSERT

    @JvmStatic
    val T_DIRECTION_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_DIRECTION)

    @Deprecated("use T_DIRECTION_SQL_DROP instead", ReplaceWith("T_DIRECTION_SQL_DROP"))
    @JvmStatic
    val T_TRIP_SQL_DROP = T_DIRECTION_SQL_DROP

    // endregion Direction

    // region Stop

    const val T_STOP = "stop"
    const val T_STOP_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_STOP_K_CODE = "code" // optional
    const val T_STOP_K_NAME = "name"
    const val T_STOP_K_LAT = "lat"
    const val T_STOP_K_LNG = "lng"
    const val T_STOP_K_ACCESSIBLE = "a11y"
    const val T_STOP_K_ORIGINAL_ID_HASH = "o_id_hash"

    @JvmStatic
    val T_STOP_SQL_CREATE = SQLCreateBuilder.getNew(T_STOP).apply {
        appendColumn(T_STOP_K_ID, SQLUtils.INT_PK)
        appendColumn(T_STOP_K_CODE, SQLUtils.TXT)
        appendColumn(T_STOP_K_NAME, SQLUtils.TXT)
        appendColumn(T_STOP_K_LAT, SQLUtils.REAL)
        appendColumn(T_STOP_K_LNG, SQLUtils.REAL)
        appendColumn(T_STOP_K_ACCESSIBLE, SQLUtils.INT)
        appendColumn(T_STOP_K_ORIGINAL_ID_HASH, SQLUtils.INT)
    }.build()

    @JvmStatic
    val T_STOP_SQL_INSERT = SQLInsertBuilder.getNew(T_STOP).apply {
        appendColumn(T_STOP_K_ID)
        appendColumn(T_STOP_K_CODE)
        appendColumn(T_STOP_K_NAME)
        appendColumn(T_STOP_K_LAT)
        appendColumn(T_STOP_K_LNG)
        appendColumn(T_STOP_K_ACCESSIBLE)
        appendColumn(T_STOP_K_ORIGINAL_ID_HASH)
    }.build()

    @JvmStatic
    val T_STOP_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_STOP)

    // endregion Stop

    // region Direction Stops

    const val T_DIRECTION_STOPS = "trip_stops" // do not change to avoid breaking compat w/ old modules
    const val T_DIRECTION_STOPS_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_DIRECTION_STOPS_K_DIRECTION_ID = "trip_id" // do not change to avoid breaking compat w/ old modules
    const val T_DIRECTION_STOPS_K_STOP_ID = "stop_id"
    const val T_DIRECTION_STOPS_K_STOP_SEQUENCE = "stop_sequence"
    const val T_DIRECTION_STOPS_K_NO_PICKUP = "decent_only"

    @Deprecated("use T_DIRECTION_STOPS instead", ReplaceWith("T_DIRECTION_STOPS"))
    const val T_TRIP_STOPS = T_DIRECTION_STOPS
    @Deprecated("use T_DIRECTION_STOPS_K_ID instead", ReplaceWith("T_DIRECTION_STOPS_K_ID"))
    const val T_TRIP_STOPS_K_ID = T_DIRECTION_STOPS_K_ID
    @Deprecated("use T_DIRECTION_STOPS_K_DIRECTION_ID instead", ReplaceWith("T_DIRECTION_STOPS_K_DIRECTION_ID"))
    const val T_TRIP_STOPS_K_TRIP_ID = T_DIRECTION_STOPS_K_DIRECTION_ID
    @Deprecated("use T_DIRECTION_STOPS_K_STOP_ID instead", ReplaceWith("T_DIRECTION_STOPS_K_STOP_ID"))
    const val T_TRIP_STOPS_K_STOP_ID = T_DIRECTION_STOPS_K_STOP_ID
    @Deprecated("use T_DIRECTION_STOPS_K_STOP_SEQUENCE instead", ReplaceWith("T_DIRECTION_STOPS_K_STOP_SEQUENCE"))
    const val T_TRIP_STOPS_K_STOP_SEQUENCE = T_DIRECTION_STOPS_K_STOP_SEQUENCE
    @Deprecated("use T_DIRECTION_STOPS_K_NO_PICKUP instead", ReplaceWith("T_DIRECTION_STOPS_K_NO_PICKUP"))
    const val T_TRIP_STOPS_K_NO_PICKUP = T_DIRECTION_STOPS_K_NO_PICKUP

    @JvmStatic
    val T_DIRECTION_STOPS_SQL_CREATE = SQLCreateBuilder.getNew(T_DIRECTION_STOPS).apply {
        appendColumn(T_DIRECTION_STOPS_K_ID, SQLUtils.INT_PK_AUTO)
        appendColumn(T_DIRECTION_STOPS_K_DIRECTION_ID, SQLUtils.INT)
        appendColumn(T_DIRECTION_STOPS_K_STOP_ID, SQLUtils.INT)
        appendColumn(T_DIRECTION_STOPS_K_STOP_SEQUENCE, SQLUtils.INT)
        appendColumn(T_DIRECTION_STOPS_K_NO_PICKUP, SQLUtils.INT)
        appendForeignKey(T_DIRECTION_STOPS_K_DIRECTION_ID, T_DIRECTION, T_DIRECTION_K_ID)
        appendForeignKey(T_DIRECTION_STOPS_K_STOP_ID, T_STOP, T_STOP_K_ID)
    }.build()

    @Deprecated("use T_DIRECTION_STOPS_SQL_CREATE instead", ReplaceWith("T_DIRECTION_STOPS_SQL_CREATE"))
    @JvmStatic
    val T_TRIP_STOPS_SQL_CREATE = T_DIRECTION_STOPS_SQL_CREATE

    @JvmStatic
    val T_DIRECTION_STOPS_SQL_INSERT = SQLInsertBuilder.getNew(T_DIRECTION_STOPS).apply {
        appendColumn(T_DIRECTION_STOPS_K_DIRECTION_ID)
        appendColumn(T_DIRECTION_STOPS_K_STOP_ID)
        appendColumn(T_DIRECTION_STOPS_K_STOP_SEQUENCE)
        appendColumn(T_DIRECTION_STOPS_K_NO_PICKUP)
    }.build()

    @Deprecated("use T_DIRECTION_STOPS_SQL_INSERT instead", ReplaceWith("T_DIRECTION_STOPS_SQL_INSERT"))
    @JvmStatic
    val T_TRIP_STOPS_SQL_INSERT = T_DIRECTION_STOPS_SQL_INSERT

    @JvmStatic
    val T_DIRECTION_STOPS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_DIRECTION_STOPS)

    @Deprecated("use T_DIRECTION_STOPS_SQL_DROP instead", ReplaceWith("T_DIRECTION_STOPS_SQL_DROP"))
    @JvmStatic
    val T_TRIP_STOPS_SQL_DROP = T_DIRECTION_STOPS_SQL_DROP

    // endregion Direction Stops

    // region Service Dates

    const val T_SERVICE_DATES = "service_dates"
    const val T_SERVICE_DATES_K_SERVICE_ID = "service_id"
    const val T_SERVICE_DATES_K_DATE = "date"
    const val T_SERVICE_DATES_K_EXCEPTION_TYPE = "exception_type"

    @JvmStatic
    val T_SERVICE_DATES_SQL_CREATE = SQLCreateBuilder.getNew(T_SERVICE_DATES).apply {
        appendColumn(T_SERVICE_DATES_K_SERVICE_ID, SQLUtils.TXT)
        appendColumn(T_SERVICE_DATES_K_DATE, SQLUtils.INT)
        if (FeatureFlags.F_EXPORT_SERVICE_EXCEPTION_TYPE) {
            appendColumn(T_SERVICE_DATES_K_EXCEPTION_TYPE, SQLUtils.INT)
        }
    }.build()

    @JvmStatic
    val T_SERVICE_DATES_SQL_INSERT = SQLInsertBuilder.getNew(T_SERVICE_DATES).apply {
        appendColumn(T_SERVICE_DATES_K_SERVICE_ID)
        appendColumn(T_SERVICE_DATES_K_DATE)
        if (FeatureFlags.F_EXPORT_SERVICE_EXCEPTION_TYPE) {
            appendColumn(T_SERVICE_DATES_K_EXCEPTION_TYPE)
        }
    }.build()

    const val EXCEPTION_TYPE_DEFAULT = 0 // default schedule
    const val EXCEPTION_TYPE_ADDED = 1
    const val EXCEPTION_TYPE_REMOVED = 2

    @JvmStatic
    val T_SERVICE_DATES_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_SERVICE_DATES)

    // endregion Service Dates

    @JvmField
    val DEFAULT_ID_HASH: Int? = null

    @JvmStatic
    fun stringIdToHashIfEnabled(originalId: String, idCleanupRegex: Pattern? = null) =
        stringIdToHash(originalId, idCleanupRegex)

    @JvmStatic
    @JvmOverloads
    fun stringIdToHash(originalId: String, idCleanupRegex: Pattern? = null): Int {
        return CleanUtils.cleanMergedID(
            cleanOriginalId(originalId, idCleanupRegex)
        ).hashCode()
    }

    @JvmStatic
    fun cleanOriginalId(originalId: String, idCleanupRegex: Pattern? = null) =
        idCleanupRegex?.matcher(originalId)?.replaceAll(EMPTY) ?: originalId

    @JvmStatic
    fun makeIdCleanupPattern(idCleanupRegex: String? = null) =
        idCleanupRegex?.takeIf { it.isNotEmpty() }?.let { Pattern.compile(it) }

    @JvmField
    val DEFAULT_ROUTE_TYPE: Int? = null

    // https://developers.google.com/transit/gtfs/reference/extended-route-types
    // FR: Taxi collectif, (sur réservation)
    @JvmField
    val ROUTE_TYPES_REQUIRES_BOOKING = setOf(
        715, // Demand and Response Bus Service
        717, // Share Taxi Service // REMOVED
        1501, // Communal Taxi Service
    )
}