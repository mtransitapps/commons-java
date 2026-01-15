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

    @JvmField
    val T_ROUTE_STRINGS_COLUMN_IDX = intArrayOf(1, 2)

    @JvmStatic
    val T_ROUTE_SQL_CREATE = SQLCreateBuilder.getNew(T_ROUTE).apply {
        appendColumn(T_ROUTE_K_ID, SQLUtils.INT_PK)
        appendColumn(T_ROUTE_K_SHORT_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_LONG_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_COLOR, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ORIGINAL_ID_HASH, SQLUtils.INT)
        appendColumn(T_ROUTE_K_TYPE, SQLUtils.INT)
    }.build()

    @JvmStatic
    val T_ROUTE_SQL_INSERT = SQLInsertBuilder.getNew(T_ROUTE).apply {
        appendColumn(T_ROUTE_K_ID)
        appendColumn(T_ROUTE_K_SHORT_NAME)
        appendColumn(T_ROUTE_K_LONG_NAME)
        appendColumn(T_ROUTE_K_COLOR)
        appendColumn(T_ROUTE_K_ORIGINAL_ID_HASH)
        appendColumn(T_ROUTE_K_TYPE)
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

    @JvmField
    val T_DIRECTION_STRINGS_COLUMN_IDX = intArrayOf(2)

    @JvmStatic
    val T_DIRECTION_SQL_CREATE = SQLCreateBuilder.getNew(T_DIRECTION).apply {
        appendColumn(T_DIRECTION_K_ID, SQLUtils.INT_PK)
        appendColumn(T_DIRECTION_K_HEADSIGN_TYPE, SQLUtils.INT)
        appendColumn(T_DIRECTION_K_HEADSIGN_VALUE, SQLUtils.TXT)
        appendColumn(T_DIRECTION_K_ROUTE_ID, SQLUtils.INT)
        appendForeignKey(T_DIRECTION_K_ROUTE_ID, T_ROUTE, T_ROUTE_K_ID)
    }.build()

    @JvmStatic
    val T_DIRECTION_SQL_INSERT = SQLInsertBuilder.getNew(T_DIRECTION).apply {
        appendColumn(T_DIRECTION_K_ID)
        appendColumn(T_DIRECTION_K_HEADSIGN_TYPE)
        appendColumn(T_DIRECTION_K_HEADSIGN_VALUE)
        appendColumn(T_DIRECTION_K_ROUTE_ID)
    }.build()

    @JvmStatic
    val T_DIRECTION_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_DIRECTION)

    // endregion Direction

    // region Trip

    const val T_TRIP = "path" // do not change to avoid breaking compat w/ old modules
    const val T_TRIP_K_ROUTE_ID = "route_id"
    private const val T_TRIP_K_SERVICE_ID = "service_id"
    private const val T_TRIP_K_SERVICE_ID_INT = "service_id_int"

    @JvmStatic
    val T_TRIP_K_SERVICE_ID_OR_INT = if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) T_TRIP_K_SERVICE_ID_INT else T_TRIP_K_SERVICE_ID
    private const val T_TRIP_K_TRIP_ID = "trip_id"
    private const val T_TRIP_K_TRIP_ID_INT = "trip_id_int"

    @JvmStatic
    val T_TRIP_K_TRIP_ID_OR_INT = if (FeatureFlags.F_EXPORT_TRIP_ID_INTS) T_TRIP_K_TRIP_ID_INT else T_TRIP_K_TRIP_ID
    const val T_TRIP_K_DIRECTION_ID = "direction_id"

    const val T_TRIP_SAME_COLUMNS_COUNT = 3
    const val T_TRIP_OTHER_COLUMNS_COUNT = 1

    @JvmStatic
    val T_TRIP_SQL_CREATE = SQLCreateBuilder.getNew(T_TRIP).apply {
        if (FeatureFlags.F_EXPORT_TRIP_ID_INTS) {
            appendColumn(T_TRIP_K_TRIP_ID_INT, SQLUtils.INT)
        } else {
            appendColumn(T_TRIP_K_TRIP_ID, SQLUtils.TXT)
        }
        appendColumn(T_TRIP_K_ROUTE_ID, SQLUtils.INT)
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendColumn(T_TRIP_K_SERVICE_ID_INT, SQLUtils.INT)
        } else {
            appendColumn(T_TRIP_K_SERVICE_ID, SQLUtils.TXT)
        }
        appendColumn(T_TRIP_K_DIRECTION_ID, SQLUtils.INT)
        appendForeignKey(T_TRIP_K_ROUTE_ID, T_ROUTE, T_ROUTE_K_ID)
        appendForeignKey(T_TRIP_K_DIRECTION_ID, T_DIRECTION, T_DIRECTION_K_ID)
        if (FeatureFlags.F_EXPORT_TRIP_ID_INTS) {
            appendForeignKey(T_TRIP_K_TRIP_ID_INT, T_TRIP_IDS, T_TRIP_IDS_K_ID_INT)
        }
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendForeignKey(T_TRIP_K_SERVICE_ID_INT, T_SERVICE_IDS, T_SERVICE_IDS_K_ID_INT)
        }
    }.build()

    @JvmStatic
    val T_TRIP_SQL_INSERT = SQLInsertBuilder.getNew(T_TRIP).apply {
        appendColumn(T_TRIP_K_ROUTE_ID)
        appendColumn(T_TRIP_K_DIRECTION_ID)
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendColumn(T_TRIP_K_SERVICE_ID_INT)
        } else {
            appendColumn(T_TRIP_K_SERVICE_ID)
        }
        if (FeatureFlags.F_EXPORT_TRIP_ID_INTS) {
            appendColumn(T_TRIP_K_TRIP_ID_INT)
        } else {
            appendColumn(T_TRIP_K_TRIP_ID)
        }
    }.build()

    @JvmStatic
    val T_TRIP_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_TRIP)

    // endregion Trip

    // region Trip IDs

    const val T_TRIP_IDS = "trip_ids"
    const val T_TRIP_IDS_K_ID_INT = "trip_id_int"
    const val T_TRIP_IDS_K_ID = "trip_id"

    @JvmStatic
    val T_TRIP_IDS_SQL_CREATE = SQLCreateBuilder.getNew(T_TRIP_IDS).apply {
        appendColumn(T_TRIP_IDS_K_ID_INT, SQLUtils.INT_PK_AUTO) // can manual insert, next ID based on largest value in table
        appendColumn(T_TRIP_IDS_K_ID, SQLUtils.TXT, unique = true)
    }.build()

    @JvmStatic
    val T_TRIP_IDS_SQL_INSERT = SQLInsertBuilder.getNew(T_TRIP_IDS).apply {
        appendColumn(T_TRIP_IDS_K_ID_INT)
        appendColumn(T_TRIP_IDS_K_ID)
    }.build()

    @JvmStatic
    val T_TRIP_IDS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_TRIP_IDS)

    // endregion Trip IDs

    // region Stop

    const val T_STOP = "stop"
    const val T_STOP_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_STOP_K_CODE = "code" // optional
    const val T_STOP_K_NAME = "name"
    const val T_STOP_K_LAT = "lat"
    const val T_STOP_K_LNG = "lng"
    const val T_STOP_K_ACCESSIBLE = "a11y"
    const val T_STOP_K_ORIGINAL_ID_HASH = "o_id_hash"

    @JvmField
    val T_STOP_STRINGS_COLUMN_IDX = intArrayOf(2)

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

    @JvmStatic
    val T_DIRECTION_STOPS_SQL_INSERT = SQLInsertBuilder.getNew(T_DIRECTION_STOPS).apply {
        appendColumn(T_DIRECTION_STOPS_K_DIRECTION_ID)
        appendColumn(T_DIRECTION_STOPS_K_STOP_ID)
        appendColumn(T_DIRECTION_STOPS_K_STOP_SEQUENCE)
        appendColumn(T_DIRECTION_STOPS_K_NO_PICKUP)
    }.build()

    @JvmStatic
    val T_DIRECTION_STOPS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_DIRECTION_STOPS)

    // endregion Direction Stops

    // region Service IDs

    const val T_SERVICE_IDS = "service_ids"
    const val T_SERVICE_IDS_K_ID_INT = "service_id_int"
    const val T_SERVICE_IDS_K_ID = "service_id"

    @JvmStatic
    val T_SERVICE_IDS_SQL_CREATE = SQLCreateBuilder.getNew(T_SERVICE_IDS).apply {
        appendColumn(T_SERVICE_IDS_K_ID_INT, SQLUtils.INT_PK_AUTO) // can manual insert, next ID based on largest value in table
        appendColumn(T_SERVICE_IDS_K_ID, SQLUtils.TXT, unique = true)
    }.build()

    @JvmStatic
    val T_SERVICE_IDS_SQL_INSERT = SQLInsertBuilder.getNew(T_SERVICE_IDS).apply {
        appendColumn(T_SERVICE_IDS_K_ID_INT)
        appendColumn(T_SERVICE_IDS_K_ID)
    }.build()

    @JvmStatic
    val T_SERVICE_IDS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_SERVICE_IDS)

    // endregion Service IDs

    // region Service Dates

    const val T_SERVICE_DATES = "service_dates"
    const val T_SERVICE_DATES_K_SERVICE_ID = "service_id"
    const val T_SERVICE_DATES_K_SERVICE_ID_INT = "service_id_int"
    const val T_SERVICE_DATES_K_DATE = "date"
    const val T_SERVICE_DATES_K_EXCEPTION_TYPE = "exception_type"

    @JvmStatic
    val T_SERVICE_DATES_SAME_COLUMNS_COUNT = if (FeatureFlags.F_EXPORT_FLATTEN_SERVICE_DATES) 1 else 0

    @JvmStatic
    val T_SERVICE_DATES_OTHER_COLUMNS_COUNT = if (FeatureFlags.F_EXPORT_FLATTEN_SERVICE_DATES) 2 else 0

    // https://gtfs.org/documentation/schedule/reference/#calendar_datestxt
    const val EXCEPTION_TYPE_DEFAULT = 0 // default schedule // added by MT
    const val EXCEPTION_TYPE_ADDED = 1
    const val EXCEPTION_TYPE_REMOVED = 2

    @JvmStatic
    val T_SERVICE_DATES_SQL_CREATE = SQLCreateBuilder.getNew(T_SERVICE_DATES).apply {
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendColumn(T_SERVICE_DATES_K_SERVICE_ID_INT, SQLUtils.INT)
        } else {
            appendColumn(T_SERVICE_DATES_K_SERVICE_ID, SQLUtils.TXT)
        }
        appendColumn(T_SERVICE_DATES_K_DATE, SQLUtils.INT)
        appendColumn(T_SERVICE_DATES_K_EXCEPTION_TYPE, SQLUtils.INT)
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendForeignKey(T_SERVICE_DATES_K_SERVICE_ID_INT, T_SERVICE_IDS, T_SERVICE_IDS_K_ID_INT)
        }
    }.build()

    @JvmStatic
    val T_SERVICE_DATES_SQL_INSERT = SQLInsertBuilder.getNew(T_SERVICE_DATES).apply {
        if (FeatureFlags.F_EXPORT_SERVICE_ID_INTS) {
            appendColumn(T_SERVICE_DATES_K_SERVICE_ID_INT)
        } else {
            appendColumn(T_SERVICE_DATES_K_SERVICE_ID)
        }
        appendColumn(T_SERVICE_DATES_K_DATE)
        appendColumn(T_SERVICE_DATES_K_EXCEPTION_TYPE)
    }.build()

    @JvmStatic
    val T_SERVICE_DATES_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_SERVICE_DATES)

    // endregion Service Dates

    // region Strings

    const val STRINGS_SEPARATOR = " "

    const val T_STRINGS = "strings"
    const val T_STRINGS_K_ID = SQLUtils.BASE_COLUMNS_ID
    const val T_STRINGS_K_STRING = "string"

    @JvmStatic
    val T_STRINGS_SQL_CREATE = SQLCreateBuilder.getNew(T_STRINGS).apply {
        appendColumn(T_STRINGS_K_ID, SQLUtils.INT_PK_AUTO) // can manual insert, next ID based on largest value in table
        appendColumn(T_STRINGS_K_STRING, SQLUtils.TXT, unique = true)
    }.build()

    @JvmStatic
    val T_STRINGS_SQL_INSERT = SQLInsertBuilder.getNew(T_STRINGS).apply {
        appendColumn(T_STRINGS_K_ID) // need to insert known string IDS+Int (Foreign Key used in other tables)
        appendColumn(T_STRINGS_K_STRING)
    }.build()

    @JvmStatic
    val T_STRINGS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_STRINGS)

    // endregion Strings

    @JvmField
    val DEFAULT_ID_HASH: Int? = null

    @JvmStatic
    @JvmOverloads
    fun originalIdToId(originalId: String, idCleanupRegex: Pattern? = null) =
        CleanUtils.cleanMergedID(
            cleanOriginalId(originalId, idCleanupRegex)
        )

    @JvmStatic
    @JvmOverloads
    fun stringIdToHash(originalId: String, idCleanupRegex: Pattern? = null): Int {
        return originalIdToId(originalId, idCleanupRegex).hashCode()
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