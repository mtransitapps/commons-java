package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Frequency
import org.mtransit.commons.gtfs.data.TripId
import org.mtransit.commons.sql.SQLUtils
import java.sql.ResultSet
import java.sql.Statement

object FrequencySQL : CommonSQL<Frequency>(), TableSQL {

    const val T_FREQUENCY = "frequencies"

    const val T_FREQUENCY_K_TRIP_ID_INT = "trip_id_int"
    const val T_FREQUENCY_K_START_TIME = "start_time"
    const val T_FREQUENCY_K_END_TIME = "end_time"
    const val T_FREQUENCY_K_HEADWAY_SECS = "headway_secs"
    const val T_FREQUENCY_K_EXACT_TIMES = "exact_times"

    override fun getMainTable() = SQLTableDef(
        T_FREQUENCY,
        listOf(
            SQLColumDef(T_FREQUENCY_K_TRIP_ID_INT, SQLUtils.INT, primaryKey = true, SQLForeignKey(TripSQL.T_TRIP_IDS, TripSQL.T_TRIP_IDS_K_ID_INT)),
            SQLColumDef(T_FREQUENCY_K_START_TIME, SQLUtils.TXT), // TODO int
            SQLColumDef(T_FREQUENCY_K_END_TIME, SQLUtils.TXT), // TODO int
            SQLColumDef(T_FREQUENCY_K_HEADWAY_SECS, SQLUtils.INT),
            SQLColumDef(T_FREQUENCY_K_EXACT_TIMES, SQLUtils.INT),
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, frequency: Frequency) = with(frequency) {
        arrayOf<Any?>(
            TripSQL.getOrInsertIdInt(statement, frequency.tripId),
            startTime,
            endTime,
            headwaySecs,
            exactTimes,
        )
    }

    fun insert(frequency: Frequency, statement: Statement): Boolean {
        return statement.executeUpdate(
            getSQLInsertOrReplace(
                statement,
                frequency
            )
        ) > 0
    }

    override fun fromResultSet(rs: ResultSet) = with(rs) {
        Frequency(
            tripId = getString(TripSQL.T_TRIP_IDS_K_ID),
            startTime = getString(T_FREQUENCY_K_START_TIME),
            endTime = getString(T_FREQUENCY_K_END_TIME),
            headwaySecs = getInt(T_FREQUENCY_K_HEADWAY_SECS),
            exactTimes = getInt(T_FREQUENCY_K_EXACT_TIMES),
        )
    }

    fun select(statement: Statement, tripId: TripId?): List<Frequency> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_FREQUENCY ")
            append("LEFT JOIN ${TripSQL.T_TRIP_IDS} ON $T_FREQUENCY.$T_FREQUENCY_K_TRIP_ID_INT = ${TripSQL.T_TRIP_IDS}.${TripSQL.T_TRIP_IDS_K_ID_INT} ")
            tripId?.let {
                append("WHERE ${TripSQL.T_TRIP_IDS}.${TripSQL.T_TRIP_IDS_K_ID} = '$it'")
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

    fun selectFrequencyTripIds(statement: Statement): List<TripId> {
        val sql = buildString {
            append("SELECT DISTINCT ${TripSQL.T_TRIP_IDS_K_ID} ")
            append("FROM ${TripSQL.T_TRIP_IDS} ")
            append("WHERE EXISTS ( ")
            append("SELECT 1 FROM $T_FREQUENCY WHERE $T_FREQUENCY.$T_FREQUENCY_K_TRIP_ID_INT = ${TripSQL.T_TRIP_IDS}.${TripSQL.T_TRIP_IDS_K_ID_INT} ")
            append(")")
        }
        return statement.executeQuery(sql).use { rs ->
            val routeIds = mutableListOf<String>()
            while (rs.next()) {
                routeIds.add(rs.getString(TripSQL.T_TRIP_IDS_K_ID))
            }
            routeIds
        }
    }

    fun delete(statement: Statement, tripId: TripId? = null): Int {
        val sql = buildString {
            append("DELETE FROM $T_FREQUENCY")
            tripId?.let {
                append(" WHERE $T_FREQUENCY.$T_FREQUENCY_K_TRIP_ID_INT = ${TripSQL.getOrInsertIdInt(statement, tripId)}")
            }
        }
        return statement.executeUpdate(sql)
    }
}