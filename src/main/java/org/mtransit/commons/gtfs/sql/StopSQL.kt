package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Stop
import org.mtransit.commons.gtfs.data.StopId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.ResultSet
import java.sql.Statement

object StopSQL : CommonSQL<Stop>(), TableSQL {

    const val T_STOP_IDS = "stop_ids"
    const val T_STOP_IDS_K_ID_INT = "stop_id_int"
    const val T_STOP_IDS_K_ID = "stop_id"

    override fun getIdsTable() = SQLTableDef.makeIdsTable(
        tableName = T_STOP_IDS,
        columnNameIdInt = T_STOP_IDS_K_ID_INT,
        columnNameId = T_STOP_IDS_K_ID,
    )

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

    override fun getMainTable() = SQLTableDef(
        T_STOP,
        listOf(
            SQLColumDef(T_STOP_K_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(T_STOP_IDS, T_STOP_IDS_K_ID_INT)),
            SQLColumDef(T_STOP_K_STOP_CODE, SQLUtils.TXT),
            SQLColumDef(T_STOP_K_STOP_NAME, SQLUtils.TXT),
            SQLColumDef(T_STOP_K_STOP_LAT, SQLUtils.REAL),
            SQLColumDef(T_STOP_K_STOP_LON, SQLUtils.REAL),
            SQLColumDef(T_STOP_K_STOP_URL, SQLUtils.TXT),
            SQLColumDef(T_STOP_K_LOCATION_TYPE, SQLUtils.INT),
            SQLColumDef(T_STOP_K_PARENT_STATION_ID_INT, SQLUtils.INT, foreignKey = SQLForeignKey(T_STOP_IDS, T_STOP_IDS_K_ID_INT)),
            SQLColumDef(T_STOP_K_WHEELCHAIR_BOARDING, SQLUtils.INT),
        )
    )

    override fun toInsertColumns(idInt: Int, stop: Stop) = with(stop) {
        arrayOf<Any?>(
            idInt,
            stopCode?.quotesEscape(),
            stopName.quotesEscape(),
            stopLat,
            stopLon,
            stopUrl?.quotesEscape(),
            locationType,
            parentStationId?.let { getSQLSelectIdIntFromId(it) },
            wheelchairBoarding,
        )
    }

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