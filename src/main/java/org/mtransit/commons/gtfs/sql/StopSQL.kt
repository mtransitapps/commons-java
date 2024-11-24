package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Stop
import org.mtransit.commons.gtfs.data.StopId
import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.ResultSet
import java.sql.Statement

object StopSQL {

    const val T_STOP_IDS = "stop_ids"
    const val T_STOP_IDS_K_ID_INT = "stop_id_int"
    const val T_STOP_IDS_K_ID = "stop_id"

    @JvmStatic
    val T_STOP_IDS_SQL_CREATE = SQLCreateBuilder.getNew(T_STOP_IDS).apply {
        appendColumn(T_STOP_IDS_K_ID_INT, SQLUtils.INT_PK_AUTO)
        appendColumn(T_STOP_IDS_K_ID, SQLUtils.TXT)
    }.build()

    @JvmStatic
    val T_STOP_IDS_SQL_INSERT = SQLInsertBuilder.getNew(T_STOP_IDS).apply {
        // appendColumn(T_STOP_IDS_K_ID_INT) // AUTOINCREMENT
        appendColumn(T_STOP_IDS_K_ID)
    }.build()

    @JvmStatic
    val T_STOP_IDS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_STOP_IDS)

    const val T_STOP = "stop"

    const val T_STOP_K_ID_INT = "stop_id_int"
    const val T_STOP_K_STOP_CODE = "stop_code"
    const val T_STOP_K_STOP_NAME = "stop_name"
    const val T_STOP_K_STOP_LAT = "stop_lat"
    const val T_STOP_K_STOP_LON = "stop_lon"
    const val T_STOP_K_STOP_URL = "stop_url"
    const val T_STOP_K_LOCATION_TYPE = "location_type"
    const val T_STOP_K_PARENT_STATION_ID_INT = "parent_station_id_int"
    const val T_STOP_K_WHEELCHAIR_BOARDING = "wheelchair_boarding"

    @JvmStatic
    val T_STOP_SQL_CREATE = SQLCreateBuilder.getNew(T_STOP).apply {
        appendColumn(T_STOP_K_ID_INT, SQLUtils.INT)
        appendColumn(T_STOP_K_STOP_CODE, SQLUtils.TXT)
        appendColumn(T_STOP_K_STOP_NAME, SQLUtils.TXT)
        appendColumn(T_STOP_K_STOP_LAT, SQLUtils.REAL)
        appendColumn(T_STOP_K_STOP_LON, SQLUtils.REAL)
        appendColumn(T_STOP_K_STOP_URL, SQLUtils.TXT)
        appendColumn(T_STOP_K_LOCATION_TYPE, SQLUtils.INT)
        appendColumn(T_STOP_K_PARENT_STATION_ID_INT, SQLUtils.INT)
        appendColumn(T_STOP_K_WHEELCHAIR_BOARDING, SQLUtils.INT)
            .appendPrimaryKeys(
                T_STOP_K_ID_INT,
            ).appendForeignKey(
                T_STOP_K_ID_INT, T_STOP_IDS, T_STOP_IDS_K_ID_INT
            ).appendForeignKey(
                T_STOP_K_PARENT_STATION_ID_INT, T_STOP_IDS, T_STOP_IDS_K_ID_INT
            )
    }.build()

    @JvmStatic
    val T_STOP_SQL_INSERT_OR_REPLACE = SQLInsertBuilder.getNew(T_STOP, allowReplace = true).apply {
        appendColumn(T_STOP_K_ID_INT)
        appendColumn(T_STOP_K_STOP_CODE)
        appendColumn(T_STOP_K_STOP_NAME)
        appendColumn(T_STOP_K_STOP_LAT)
        appendColumn(T_STOP_K_STOP_LON)
        appendColumn(T_STOP_K_STOP_URL)
        appendColumn(T_STOP_K_LOCATION_TYPE)
        appendColumn(T_STOP_K_PARENT_STATION_ID_INT)
        appendColumn(T_STOP_K_WHEELCHAIR_BOARDING)
    }.build()

    @JvmStatic
    val T_STOP_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_STOP)

    fun getSQLCreateTablesQueries() = listOf(T_STOP_IDS_SQL_CREATE, T_STOP_SQL_CREATE)

    fun getSQLDropIfExistsQueries() = listOf(T_STOP_IDS_SQL_DROP, T_STOP_SQL_DROP)

    private fun getSQLInsertIds(stopId: StopId) = SQLInsertBuilder.compile(
        T_STOP_IDS_SQL_INSERT,
        stopId.quotesEscape()
    )

    private fun getSQLSelectIdIntFromId(stopId: StopId) =
        "SELECT $T_STOP_IDS_K_ID_INT FROM $T_STOP_IDS WHERE $T_STOP_IDS_K_ID = '$stopId'"

    private fun getSQLInsertOrReplace(stopIdInt: Int, stop: Stop) = SQLInsertBuilder.compile(
        T_STOP_SQL_INSERT_OR_REPLACE,
        stopIdInt,
        stop.stopCode?.quotesEscape(),
        stop.stopName.quotesEscape(),
        stop.stopLat,
        stop.stopLon,
        stop.stopUrl?.quotesEscape(),
        stop.locationType,
        stop.parentStationId?.let { getSQLSelectIdIntFromId(it) },
        stop.wheelchairBoarding,
    )

    fun getOrInsertIdInt(statement: Statement, stopId: StopId): Int {
        return statement.executeQuery(getSQLSelectIdIntFromId(stopId)).use { rs ->
            if (rs.next()) {
                rs.getInt(1)
            } else {
                if (statement.executeUpdate(getSQLInsertIds(stopId)) > 0) {
                    statement.executeQuery(getSQLSelectIdIntFromId(stopId)).use { rs2 ->
                        if (rs2.next()) {
                            rs2.getInt(1)
                        } else {
                            throw Exception("Error while inserting agency ID")
                        }
                    }
                } else {
                    throw Exception("Error while inserting agency ID")
                }
            }
        }
    }

    fun insert(stop: Stop, statement: Statement): Boolean {
        return statement.executeUpdate(
            getSQLInsertOrReplace(
                getOrInsertIdInt(statement, stop.stopId),
                stop
            )
        ) > 0
    }

    fun select(stopId: StopId? = null, statement: Statement): List<Stop> {
        val sql = buildString {
            append("SELECT ")
            append(" * ")
            append("FROM $T_STOP ")
            append("LEFT JOIN $T_STOP_IDS ON $T_STOP.$T_STOP_K_ID_INT = $T_STOP_IDS.$T_STOP_K_ID_INT ")
            stopId?.let {
                append("WHERE $T_STOP_IDS.$T_STOP_IDS_K_ID = '$it'")
            }
        }
        return statement.executeQuery(sql).use { rs ->
            buildList {
                while (rs.next()) {
                    add(fromResultSet(rs))
                }
            }
        }
    }

    private fun fromResultSet(rs: ResultSet) = with(rs) {
        Stop(
            stopId = getString(T_STOP_IDS_K_ID),
            stopCode = getString(T_STOP_K_STOP_CODE),
            stopName = getString(T_STOP_K_STOP_NAME),
            stopLat = getDouble(T_STOP_K_STOP_LAT),
            stopLon = getDouble(T_STOP_K_STOP_LON),
            stopUrl = getString(T_STOP_K_STOP_URL),
            locationType = getInt(T_STOP_K_LOCATION_TYPE),
            parentStationId = getString(T_STOP_K_PARENT_STATION_ID_INT),
            wheelchairBoarding = getInt(T_STOP_K_WHEELCHAIR_BOARDING),
        )
    }

    fun count(statement: Statement): Int {
        val sql = "SELECT COUNT(*) AS count FROM $T_STOP"
        return statement.executeQuery(sql).use { rs ->
            if (rs.next()) {
                return rs.getInt("count")
            }
            throw Exception("Error while counting routes!")
        }
    }

    fun delete(statement: Statement, notLocationType: Int? = null): Int {
        val sql = buildString {
            append("DELETE FROM $T_STOP")
            notLocationType?.let {
                append(" WHERE $T_STOP_K_LOCATION_TYPE != $it")
            }
        }
        return statement.executeUpdate(sql)
    }
}