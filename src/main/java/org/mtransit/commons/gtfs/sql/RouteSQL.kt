package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.AgencyId
import org.mtransit.commons.gtfs.data.Route
import org.mtransit.commons.gtfs.data.RouteId
import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.Statement

object RouteSQL {

    const val T_ROUTE_IDS = "route_ids"
    const val T_ROUTE_IDS_K_ID_INT = "route_id_int"
    const val T_ROUTE_IDS_K_ID = "route_id"

    @JvmStatic
    val T_ROUTE_IDS_SQL_CREATE = SQLCreateBuilder.getNew(T_ROUTE_IDS).apply {
        appendColumn(T_ROUTE_IDS_K_ID_INT, SQLUtils.INT_PK_AUTO)
        appendColumn(T_ROUTE_IDS_K_ID, SQLUtils.TXT)
    }.build()

    @JvmStatic
    val T_ROUTE_IDS_SQL_INSERT = SQLInsertBuilder.getNew(T_ROUTE_IDS).apply {
        // appendColumn(T_ROUTE_IDS_K_ID_INT) // AUTOINCREMENT
        appendColumn(T_ROUTE_IDS_K_ID)
    }.build()

    @JvmStatic
    val T_ROUTE_IDS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_ROUTE_IDS)


    const val T_ROUTE = "route"

    const val T_ROUTE_K_AGENCY_ID_INT = "agency_id_int"
    const val T_ROUTE_K_ID_INT = "route_id_int"
    const val T_ROUTE_K_ROUTE_SHORT_NAME = "route_short_name"
    const val T_ROUTE_K_ROUTE_LONG_NAME = "route_long_name"
    const val T_ROUTE_K_ROUTE_DESC = "route_desc"
    const val T_ROUTE_K_ROUTE_TYPE = "route_type"
    const val T_ROUTE_K_ROUTE_URL = "route_url"
    const val T_ROUTE_K_ROUTE_COLOR = "route_color"
    const val T_ROUTE_K_ROUTE_TEXT_COLOR = "route_text_color"
    const val T_ROUTE_K_ROUTE_SORT_ORDER = "route_sort_order"

    @JvmStatic
    val T_ROUTE_SQL_CREATE = SQLCreateBuilder.getNew(T_ROUTE).apply {
        appendColumn(T_ROUTE_K_AGENCY_ID_INT, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ID_INT, SQLUtils.INT)
        appendColumn(T_ROUTE_K_ROUTE_SHORT_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_LONG_NAME, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_DESC, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_TYPE, SQLUtils.INT)
        appendColumn(T_ROUTE_K_ROUTE_URL, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_COLOR, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_TEXT_COLOR, SQLUtils.TXT)
        appendColumn(T_ROUTE_K_ROUTE_SORT_ORDER, SQLUtils.INT)
            .appendPrimaryKeys(
                T_ROUTE_K_ID_INT,
            ).appendForeignKey(
                T_ROUTE_K_ID_INT, T_ROUTE_IDS, T_ROUTE_IDS_K_ID_INT
            )
    }.build()

    @JvmStatic
    val T_ROUTE_SQL_INSERT_OR_REPLACE = SQLInsertBuilder.getNew(T_ROUTE, allowReplace = true).apply {
        appendColumn(T_ROUTE_K_AGENCY_ID_INT)
        appendColumn(T_ROUTE_K_ID_INT)
        appendColumn(T_ROUTE_K_ROUTE_SHORT_NAME)
        appendColumn(T_ROUTE_K_ROUTE_LONG_NAME)
        appendColumn(T_ROUTE_K_ROUTE_DESC)
        appendColumn(T_ROUTE_K_ROUTE_TYPE)
        appendColumn(T_ROUTE_K_ROUTE_URL)
        appendColumn(T_ROUTE_K_ROUTE_COLOR)
        appendColumn(T_ROUTE_K_ROUTE_TEXT_COLOR)
        appendColumn(T_ROUTE_K_ROUTE_SORT_ORDER)
    }.build()

    @JvmStatic
    val T_ROUTE_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_ROUTE)

    fun getSQLCreateTablesQueries() = listOf(T_ROUTE_IDS_SQL_CREATE, T_ROUTE_SQL_CREATE)

    fun getSQLDropIfExistsQueries() = listOf(T_ROUTE_IDS_SQL_DROP, T_ROUTE_SQL_DROP)

    private fun getSQLInsertRouteIds(routeId: RouteId) = SQLInsertBuilder.compile(
        T_ROUTE_IDS_SQL_INSERT,
        routeId.quotesEscape()
    )

    private fun getSQLSelectRouteIdIntFromRouteId(routeId: RouteId) =
        "SELECT $T_ROUTE_IDS_K_ID_INT FROM $T_ROUTE_IDS WHERE $T_ROUTE_IDS_K_ID = '$routeId'"

    private fun getSQLInsertOrReplaceRoute(agencyIdInt: Int, routeIdInt: Int, route: Route) = SQLInsertBuilder.compile(
        T_ROUTE_SQL_INSERT_OR_REPLACE,
        agencyIdInt, // 1st
        routeIdInt, // 2nd
        route.routeShortName.quotesEscape(),
        route.routeLongName?.quotesEscape(),
        route.routeDesc?.quotesEscape(),
        route.routeType,
        route.routeUrl?.quotesEscape(),
        route.routeColor?.quotesEscape(),
        route.routeTextColor?.quotesEscape(),
        route.routeSortOrder
    )

    fun insert(route: Route, statement: Statement): Boolean {
        return statement.executeUpdate(getSQLInsertOrReplaceRoute(
            agencyIdInt = AgencySQL.getOrInsertAgencyIdInt(statement, route.agencyId),
            routeIdInt = getOrInsertRouteIdInt(statement, route.routeId),
            route = route,
        )) > 0
    }

    private fun getOrInsertRouteIdInt(statement: Statement, routeId: RouteId): Int {
        val routeIdInt = statement.executeQuery(getSQLSelectRouteIdIntFromRouteId(routeId)).use { rs ->
            if (rs.next()) {
                rs.getInt(1)
            } else {
                if (statement.executeUpdate(getSQLInsertRouteIds(routeId)) > 0) {
                    statement.executeQuery(getSQLSelectRouteIdIntFromRouteId(routeId)).use { rs2 ->
                        if (rs2.next()) {
                            rs2.getInt(1)
                        } else {
                            throw Exception("Error while inserting route ID")
                        }
                    }
                } else {
                    throw Exception("Error while inserting route ID")
                }
            }
        }
        return routeIdInt
    }

    fun selectAllRouteIds(statement: Statement): List<String> {
        val sql = "SELECT $T_ROUTE_IDS_K_ID FROM $T_ROUTE_IDS"
        return statement.executeQuery(sql).use { rs ->
            val routeIds = mutableListOf<String>()
            while (rs.next()) {
                routeIds.add(rs.getString(T_ROUTE_IDS_K_ID))
            }
            routeIds
        }
    }

    fun select(
        routeIds: Collection<RouteId>? = null,
        agencyId: AgencyId? = null,
        notAgencyId: AgencyId? = null,
        statement: Statement
    ): List<Route> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_ROUTE ")
            append("LEFT JOIN $T_ROUTE_IDS ON $T_ROUTE.$T_ROUTE_K_ID_INT = $T_ROUTE_IDS.$T_ROUTE_K_ID_INT ")
            append("LEFT JOIN ${AgencySQL.T_AGENCY_IDS} ON $T_ROUTE.$T_ROUTE_K_AGENCY_ID_INT = ${AgencySQL.T_AGENCY_IDS}.${AgencySQL.T_AGENCY_K_ID_INT} ")
            routeIds?.let {
                append("WHERE $T_ROUTE_IDS.$T_ROUTE_IDS_K_ID IN (${it.joinToString { "'$it'" }}) ")
            }
            agencyId?.let {
                append("WHERE ${AgencySQL.T_AGENCY_IDS}.${AgencySQL.T_AGENCY_IDS_K_ID} = '$it' ")
            }
            notAgencyId?.let {
                append("WHERE ${AgencySQL.T_AGENCY_IDS}.${AgencySQL.T_AGENCY_IDS_K_ID} != '$it' ")
            }
        }
        return statement.executeQuery(sql).use { rs ->
            val routes = mutableListOf<Route>()
            while (rs.next()) {
                routes.add(
                    Route(
                        routeId = rs.getString(T_ROUTE_IDS_K_ID),
                        agencyId = rs.getString(AgencySQL.T_AGENCY_IDS_K_ID),
                        routeShortName = rs.getString(T_ROUTE_K_ROUTE_SHORT_NAME),
                        routeLongName = rs.getString(T_ROUTE_K_ROUTE_LONG_NAME),
                        routeDesc = rs.getString(T_ROUTE_K_ROUTE_DESC),
                        routeType = rs.getInt(T_ROUTE_K_ROUTE_TYPE),
                        routeUrl = rs.getString(T_ROUTE_K_ROUTE_URL),
                        routeColor = rs.getString(T_ROUTE_K_ROUTE_COLOR),
                        routeTextColor = rs.getString(T_ROUTE_K_ROUTE_TEXT_COLOR),
                        routeSortOrder = rs.getInt(T_ROUTE_K_ROUTE_SORT_ORDER)
                    )
                )
            }
            routes
        }
    }

    fun count(statement: Statement): Int {
        val sql = "SELECT COUNT(*) AS count FROM $T_ROUTE"
        return statement.executeQuery(sql).use { rs ->
            if (rs.next()) {
                return rs.getInt("count")
            }
            throw Exception("Error while counting routes!")
        }
    }
}