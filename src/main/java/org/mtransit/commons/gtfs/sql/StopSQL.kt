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
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, mainObject: Stop) = with(mainObject) {
        arrayOf<Any?>(
            getOrInsertIdInt(statement, stopId),
            stopCode?.quotesEscape(),
            stopName.quotesEscape(),
            stopLat,
            stopLon,
            stopUrl?.quotesEscape(),
            locationType,
            parentStationId?.let { getOrInsertIdInt(statement, it) },
            wheelchairBoarding,
        )
    }

    fun select(stopId: StopId? = null, statement: Statement): List<Stop> {
        val sql = buildString {
            append("SELECT ")
            append("*, ")
            append("${getJoinAlias(T_STOP_K_ID_INT)}.$T_STOP_IDS_K_ID AS ${getAlias(T_STOP_K_ID_INT, T_STOP_IDS_K_ID)}, ")
            append("${getJoinAlias(T_STOP_K_PARENT_STATION_ID_INT)}.$T_STOP_IDS_K_ID AS ${getAlias(T_STOP_K_PARENT_STATION_ID_INT, T_STOP_IDS_K_ID)} ")
            append("FROM $T_STOP ")
            append("JOIN $T_STOP_IDS AS ${getJoinAlias(T_STOP_K_ID_INT)} ON $T_STOP.$T_STOP_K_ID_INT =${getJoinAlias(T_STOP_K_ID_INT)}.$T_STOP_K_ID_INT ")
            append("JOIN $T_STOP_IDS AS ${getJoinAlias(T_STOP_K_PARENT_STATION_ID_INT)} ON $T_STOP.$T_STOP_K_PARENT_STATION_ID_INT = ${getJoinAlias(T_STOP_K_PARENT_STATION_ID_INT)}.$T_STOP_K_ID_INT ")
            stopId?.let {
                append("WHERE ${getJoinAlias(T_STOP_K_ID_INT)}.$T_STOP_IDS_K_ID = '$it'")
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

    override fun fromResultSet(rs: ResultSet) = with(rs) {
        Stop(
            stopId = getString(getAlias(T_STOP_K_ID_INT, T_STOP_IDS_K_ID)),
            stopCode = getString(T_STOP_K_STOP_CODE),
            stopName = getString(T_STOP_K_STOP_NAME),
            stopLat = getDouble(T_STOP_K_STOP_LAT),
            stopLon = getDouble(T_STOP_K_STOP_LON),
            stopUrl = getString(T_STOP_K_STOP_URL),
            locationType = getInt(T_STOP_K_LOCATION_TYPE),
            parentStationId = getString(getAlias(T_STOP_K_PARENT_STATION_ID_INT, T_STOP_IDS_K_ID)),
            wheelchairBoarding = getInt(T_STOP_K_WHEELCHAIR_BOARDING),
        )
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