package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.RouteId
import org.mtransit.commons.gtfs.data.ServiceId
import org.mtransit.commons.gtfs.data.Trip
import org.mtransit.commons.gtfs.data.TripId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import org.mtransit.commons.sql.executeQueryMT
import org.mtransit.commons.sql.executeUpdateMT
import org.mtransit.commons.sql.toSQL
import java.sql.ResultSet
import java.sql.Statement
import kotlin.use

object TripSQL : CommonSQL<Trip>(), TableSQL {

    const val T_TRIP_IDS = "trip_ids"
    const val T_TRIP_IDS_K_ID_INT = "trip_id_int"
    const val T_TRIP_IDS_K_ID = "trip_id"

    override fun getIdsTable() = SQLTableDef.makeIdsTable(
        tableName = T_TRIP_IDS,
        columnNameIdInt = T_TRIP_IDS_K_ID_INT,
        columnNameId = T_TRIP_IDS_K_ID,
    )

    const val T_TRIP = "trip"

    const val T_TRIP_K_ID_INT = "trip_id_int"
    const val T_TRIP_K_ROUTE_ID_INT = "route_id_int"
    const val T_TRIP_K_SERVICE_ID_INT = "service_id_int"
    const val T_TRIP_K_TRIP_HEADSIGN = "trip_headsign"
    const val T_TRIP_K_TRIP_SHORT_NAME = "trip_short_name"
    const val T_TRIP_K_DIRECTION_ID = "direction_id"
    const val T_TRIP_K_BLOCK_ID = "block_id"
    const val T_TRIP_K_SHAPE_ID = "shape_id"
    const val T_TRIP_K_WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible"
    const val T_TRIP_K_BIKES_ALLOWED = "bikes_allowed"

    override fun getMainTable() = SQLTableDef(
        T_TRIP,
        listOf(
            SQLColumDef(T_TRIP_K_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(T_TRIP_IDS, T_TRIP_IDS_K_ID_INT)),
            SQLColumDef(T_TRIP_K_ROUTE_ID_INT, SQLUtils.INT, foreignKey = SQLForeignKey(RouteSQL.T_ROUTE, RouteSQL.T_ROUTE_K_ID_INT)),
            SQLColumDef(
                T_TRIP_K_SERVICE_ID_INT,
                SQLUtils.INT,
                foreignKey = SQLForeignKey(CalendarDateSQL.T_CALENDAR_DATE, CalendarDateSQL.T_CALENDAR_DATE_K_SERVICE_ID_INT)
            ),
            SQLColumDef(T_TRIP_K_TRIP_HEADSIGN, SQLUtils.TXT),
            SQLColumDef(T_TRIP_K_TRIP_SHORT_NAME, SQLUtils.TXT),
            SQLColumDef(T_TRIP_K_DIRECTION_ID, SQLUtils.INT),
            SQLColumDef(T_TRIP_K_BLOCK_ID, SQLUtils.TXT),
            SQLColumDef(T_TRIP_K_SHAPE_ID, SQLUtils.TXT),
            SQLColumDef(T_TRIP_K_WHEELCHAIR_ACCESSIBLE, SQLUtils.INT),
            SQLColumDef(T_TRIP_K_BIKES_ALLOWED, SQLUtils.INT),
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, mainObject: Trip) = with(mainObject) {
        arrayOf<Any?>(
            getOrInsertIdInt(statement, tripId),
            RouteSQL.getOrInsertIdInt(statement, routeId),
            CalendarDateSQL.getOrInsertIdInt(statement, serviceId),
            tripHeadsign?.quotesEscape(),
            tripShortName?.quotesEscape(),
            directionId,
            blockId?.quotesEscape(),
            shapeId?.quotesEscape(),
            wheelchairAccessible,
            bikesAllowed?.toSQL(),
        )
    }

    fun select(
        statement: Statement,
        tripIds: Collection<TripId>? = null,
        routeIds: Collection<RouteId>? = null,
        serviceId: ServiceId? = null,
    ): List<Trip> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_TRIP ")
            append("JOIN $T_TRIP_IDS ON $T_TRIP.$T_TRIP_K_ID_INT = $T_TRIP_IDS.$T_TRIP_IDS_K_ID_INT ")
            append("JOIN ${RouteSQL.T_ROUTE_IDS} ON $T_TRIP.${T_TRIP_K_ROUTE_ID_INT} = ${RouteSQL.T_ROUTE_IDS}.${RouteSQL.T_ROUTE_K_ID_INT} ")
            append("JOIN ${CalendarDateSQL.T_SERVICE_IDS} ON $T_TRIP.${T_TRIP_K_SERVICE_ID_INT} = ${CalendarDateSQL.T_SERVICE_IDS}.${CalendarDateSQL.T_SERVICE_IDS_K_ID_INT} ")
            tripIds?.let {
                append("WHERE $T_TRIP_IDS.$T_TRIP_IDS_K_ID IN (${it.joinToString { "'$it'" }}) ")
            }
            routeIds?.let {
                append("WHERE ${RouteSQL.T_ROUTE_IDS}.${RouteSQL.T_ROUTE_IDS_K_ID} IN (${it.joinToString { "'$it'" }}) ")
            }
            serviceId?.let {
                append("WHERE ${CalendarDateSQL.T_SERVICE_IDS}.${CalendarDateSQL.T_SERVICE_IDS_K_ID} = $it")
            }
        }
        return statement.executeQueryMT(sql).use { rs ->
            buildList {
                while (rs.next()) {
                    add(fromResultSet(rs))
                }
            }
        }
    }

    fun selectTripRouteIds(statement: Statement): List<RouteId> {
        val sql = buildString {
            append("SELECT DISTINCT ${RouteSQL.T_ROUTE_IDS_K_ID} ")
            append("FROM ${RouteSQL.T_ROUTE_IDS} ")
            append("JOIN $T_TRIP ON ${RouteSQL.T_ROUTE_IDS}.${RouteSQL.T_ROUTE_IDS_K_ID_INT} = $T_TRIP.$T_TRIP_K_ROUTE_ID_INT ")
        }
        return statement.executeQueryMT(sql).use { rs ->
            buildList {
                while (rs.next()) {
                    add(rs.getString(RouteSQL.T_ROUTE_IDS_K_ID))
                }
            }
        }
    }

    fun update(
        statement: Statement,
        tripIds: Collection<TripId>,
        directionId: Int,
    ): Boolean {
        val sql = buildString {
            append("UPDATE $T_TRIP ")
            append("SET $T_TRIP_K_DIRECTION_ID = $directionId ")
            append("WHERE EXISTS ( ")
            append("SELECT 1 FROM $T_TRIP, $T_TRIP_IDS WHERE $T_TRIP.$T_TRIP_K_ID_INT = $T_TRIP_IDS.$T_TRIP_IDS_K_ID_INT ")
            append("AND $T_TRIP_IDS.$T_TRIP_IDS_K_ID IN (${tripIds.joinToString { it.quotesEscape() }}) ")
            append(")")
        }
        return statement.executeUpdateMT(sql) > 0
    }

    override fun fromResultSet(rs: ResultSet) = with(rs) {
        Trip(
            tripId = rs.getString(T_TRIP_IDS_K_ID),
            routeId = rs.getString(RouteSQL.T_ROUTE_IDS_K_ID),
            serviceId = rs.getString(CalendarDateSQL.T_SERVICE_IDS_K_ID),
            tripHeadsign = rs.getString(T_TRIP_K_TRIP_HEADSIGN),
            tripShortName = rs.getString(T_TRIP_K_TRIP_SHORT_NAME),
            directionId = rs.getInt(T_TRIP_K_DIRECTION_ID),
            blockId = rs.getString(T_TRIP_K_BLOCK_ID),
            shapeId = rs.getString(T_TRIP_K_SHAPE_ID),
            wheelchairAccessible = rs.getInt(T_TRIP_K_WHEELCHAIR_ACCESSIBLE),
            bikesAllowed = rs.getBoolean(T_TRIP_K_BIKES_ALLOWED),
        )
    }

    fun delete(statement: Statement, routeId: RouteId? = null): Int {
        val sql = buildString {
            append("DELETE FROM $T_TRIP")
            routeId?.let {
                append(" WHERE $T_TRIP.$T_TRIP_K_ROUTE_ID_INT = ${RouteSQL.getOrInsertIdInt(statement, routeId)}")
            }
        }
        return statement.executeUpdateMT(sql)
    }

}