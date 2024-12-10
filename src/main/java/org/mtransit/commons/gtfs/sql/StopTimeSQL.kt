package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.StopId
import org.mtransit.commons.gtfs.data.StopTime
import org.mtransit.commons.gtfs.data.TripId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.executeUpdateMT
import java.sql.ResultSet
import java.sql.Statement

object StopTimeSQL : CommonSQL<StopTime>(), TableSQL {

    const val T_STOP_TIME = "stop_time"

    const val T_STOP_TIME_K_TRIP_ID_INT = "trip_id_int"
    const val T_STOP_TIME_K_STOP_ID_INT = "stop_id_int"
    const val T_STOP_TIME_K_STOP_SEQUENCE = "stop_sequence"
    const val T_STOP_TIME_K_ARRIVAL_TIME = "arrival_time"
    const val T_STOP_TIME_K_DEPARTURE_TIME = "departure_time"
    const val T_STOP_TIME_K_STOP_HEADSIGN = "stop_headsign"
    const val T_STOP_TIME_K_PICKUP_TYPE = "pickup_type"
    const val T_STOP_TIME_K_DROP_OFF_TYPE = "drop_off_type"
    const val T_STOP_TIME_K_TIME_POINT = "time_point"

    override fun getMainTable() = SQLTableDef(
        T_STOP_TIME,
        listOf(
            SQLColumDef(T_STOP_TIME_K_TRIP_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(TripSQL.T_TRIP, TripSQL.T_TRIP_K_ID_INT)),
            SQLColumDef(T_STOP_TIME_K_STOP_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(StopSQL.T_STOP, StopSQL.T_STOP_K_ID_INT)),
            SQLColumDef(T_STOP_TIME_K_STOP_SEQUENCE, SQLUtils.INT, primaryKey = true),
            SQLColumDef(T_STOP_TIME_K_ARRIVAL_TIME, SQLUtils.TXT), // TODO int
            SQLColumDef(T_STOP_TIME_K_DEPARTURE_TIME, SQLUtils.TXT), // TODO int
            SQLColumDef(T_STOP_TIME_K_STOP_HEADSIGN, SQLUtils.TXT),
            SQLColumDef(T_STOP_TIME_K_PICKUP_TYPE, SQLUtils.INT),
            SQLColumDef(T_STOP_TIME_K_DROP_OFF_TYPE, SQLUtils.INT),
            SQLColumDef(T_STOP_TIME_K_TIME_POINT, SQLUtils.INT),
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, stopTime: StopTime) = with(stopTime) {
        arrayOf<Any?>(
            TripSQL.getOrInsertIdInt(statement, stopTime.tripId),
            StopSQL.getOrInsertIdInt(statement, stopTime.stopId),
            stopSequence,
            arrivalTime,
            departureTime,
            stopHeadsign,
            pickupType,
            dropOffType,
            timePoint,
        )
    }

    override fun fromResultSet(rs: ResultSet) = with(rs) {
        StopTime(
            tripId = rs.getString(TripSQL.T_TRIP_IDS_K_ID),
            stopId = rs.getString(StopSQL.T_STOP_IDS_K_ID),
            stopSequence = rs.getInt(T_STOP_TIME_K_STOP_SEQUENCE),
            arrivalTime = rs.getString(T_STOP_TIME_K_ARRIVAL_TIME),
            departureTime = rs.getString(T_STOP_TIME_K_DEPARTURE_TIME),
            stopHeadsign = rs.getString(T_STOP_TIME_K_STOP_HEADSIGN),
            pickupType = rs.getInt(T_STOP_TIME_K_PICKUP_TYPE),
            dropOffType = rs.getInt(T_STOP_TIME_K_DROP_OFF_TYPE),
            timePoint = rs.getInt(T_STOP_TIME_K_TIME_POINT),
        )
    }

    fun select(statement: Statement, tripIds: Collection<TripId>? = null, limitMaxNbRow: Int? = null, limitOffset: Int? = null): List<StopTime> {
        val sql = buildString {
            append("SELECT * ")
            append("FROM $T_STOP_TIME ")
            append("JOIN ${TripSQL.T_TRIP_IDS} ON $T_STOP_TIME.$T_STOP_TIME_K_TRIP_ID_INT = ${TripSQL.T_TRIP_IDS}.${TripSQL.T_TRIP_IDS_K_ID_INT} ")
            append("JOIN ${StopSQL.T_STOP_IDS} ON $T_STOP_TIME.$T_STOP_TIME_K_STOP_ID_INT = ${StopSQL.T_STOP_IDS}.${StopSQL.T_STOP_IDS_K_ID_INT} ")
            tripIds?.let {
                append("WHERE ${TripSQL.T_TRIP_IDS}.${TripSQL.T_TRIP_IDS_K_ID} IN (${it.joinToString { "'$it'" }}) ")
            }
            append("ORDER BY ")
            append("${TripSQL.T_TRIP_IDS_K_ID} ASC, ")
            append("$T_STOP_TIME_K_STOP_SEQUENCE ASC, ")
            append("$T_STOP_TIME_K_DEPARTURE_TIME ASC ")
            limitMaxNbRow?.let {
                append("LIMIT $limitMaxNbRow ")
                limitOffset?.let {
                    append("OFFSET $limitOffset ")
                }
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

    fun delete(statement: Statement, tripId: TripId? = null, stopId: StopId? = null): Int {
        val sql = buildString {
            append("DELETE FROM $T_STOP_TIME")
            tripId?.let {
                append(" WHERE $T_STOP_TIME.$T_STOP_TIME_K_TRIP_ID_INT = ${TripSQL.getOrInsertIdInt(statement, tripId)}")
            }
            stopId?.let {
                append(" WHERE $T_STOP_TIME.$T_STOP_TIME_K_STOP_ID_INT = ${StopSQL.getOrInsertIdInt(statement, stopId)}")
            }
        }
        return statement.executeUpdateMT(sql)
    }
}