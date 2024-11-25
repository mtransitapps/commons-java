package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.AgencyId
import org.mtransit.commons.gtfs.data.Route
import org.mtransit.commons.gtfs.data.RouteId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.ResultSet
import java.sql.Statement

object RouteSQL : CommonSQL<Route>(), TableSQL {

    const val T_ROUTE_IDS = "route_ids"
    const val T_ROUTE_IDS_K_ID_INT = "route_id_int"
    const val T_ROUTE_IDS_K_ID = "route_id"

    override fun getIdsTable() = SQLTableDef.makeIdsTable(
        tableName = T_ROUTE_IDS,
        columnNameIdInt = T_ROUTE_IDS_K_ID_INT,
        columnNameId = T_ROUTE_IDS_K_ID,
    )

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

    override fun getMainTable() = SQLTableDef(
        T_ROUTE,
        listOf(
            SQLColumDef(T_ROUTE_K_AGENCY_ID_INT, SQLUtils.INT, foreignKey = SQLForeignKey(AgencySQL.T_AGENCY_IDS, AgencySQL.T_AGENCY_IDS_K_ID_INT)),
            SQLColumDef(T_ROUTE_K_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(T_ROUTE_IDS, T_ROUTE_IDS_K_ID_INT)),
            SQLColumDef(T_ROUTE_K_ROUTE_SHORT_NAME, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_LONG_NAME, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_DESC, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_TYPE, SQLUtils.INT),
            SQLColumDef(T_ROUTE_K_ROUTE_URL, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_COLOR, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_TEXT_COLOR, SQLUtils.TXT),
            SQLColumDef(T_ROUTE_K_ROUTE_SORT_ORDER, SQLUtils.INT),
        ),
        insertAllowReplace = true,
    )

    override fun toInsertColumns(statement: Statement, route: Route) = with(route) {
        arrayOf<Any?>(
            (route.agencyId.takeIf { it.isNotEmpty() } // agency ID // 1st
                ?: AgencySQL.select(null, statement).singleOrNull()?.agencyId)
                ?.let { agencyId ->
                    AgencySQL.getOrInsertIdInt(statement, agencyId)
                }
                ?: throw Exception("Can't find agency ID!"),
            getOrInsertIdInt(statement, route.routeId), // route ID // 2nd
            routeShortName.quotesEscape(),
            routeLongName?.quotesEscape(),
            routeDesc?.quotesEscape(),
            routeType,
            routeUrl?.quotesEscape(),
            routeColor?.quotesEscape(),
            routeTextColor?.quotesEscape(),
            routeSortOrder,
        )
    }

    fun insert(route: Route, statement: Statement): Boolean {
        return statement.executeUpdate(
            getSQLInsertOrReplace(
                statement,
                route,
            )
        ) > 0
    }

    fun selectAllIds(statement: Statement): List<String> {
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
        statement: Statement,
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
            buildList {
                while (rs.next()) {
                    add(fromResultSet(rs))
                }
            }
        }
    }

    override fun fromResultSet(rs: ResultSet) = with(rs) {
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
    }
}