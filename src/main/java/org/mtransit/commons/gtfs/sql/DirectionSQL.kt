package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Direction
import org.mtransit.commons.gtfs.data.DirectionId
import org.mtransit.commons.gtfs.data.DirectionType
import org.mtransit.commons.gtfs.data.RouteId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import org.mtransit.commons.sql.executeQueryMT
import java.sql.ResultSet
import java.sql.Statement

object DirectionSQL : CommonSQL<Direction>(), TableSQL {

    const val T_DIRECTION = "direction"

    const val T_DIRECTION_K_ROUTE_ID_INT = "route_id_int"
    const val T_DIRECTION_K_DIRECTION_ID = "direction_id"
    const val T_DIRECTION_K_DIRECTION_TYPE = "direction_type"
    const val T_DIRECTION_K_DESTINATION = "destination"

    override fun getMainTable() = SQLTableDef(
        T_DIRECTION,
        listOf(
            SQLColumDef(T_DIRECTION_K_ROUTE_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(RouteSQL.T_ROUTE, RouteSQL.T_ROUTE_K_ID_INT)),
            SQLColumDef(T_DIRECTION_K_DIRECTION_ID, SQLUtils.INT, primaryKey = true),
            SQLColumDef(T_DIRECTION_K_DIRECTION_TYPE, SQLUtils.TXT),
            SQLColumDef(T_DIRECTION_K_DESTINATION, SQLUtils.TXT),
        ),
        insertAllowReplace = false,
    )

     override fun toInsertColumns(statement: Statement, mainObject: Direction) = with(mainObject) {
         arrayOf<Any?>(
             RouteSQL.getOrInsertIdInt(statement, routeId),
             directionId,
             directionType?.toSQL(),
             destination?.quotesEscape(),
         )
     }

    override fun fromResultSet(rs: ResultSet) = with(rs) {
        Direction(
            routeId = rs.getString(T_DIRECTION_K_ROUTE_ID_INT),
            directionId = rs.getInt(T_DIRECTION_K_DIRECTION_ID),
            directionType = DirectionType.fromResultSet(rs, T_DIRECTION_K_DIRECTION_TYPE),
            destination = rs.getString(T_DIRECTION_K_DESTINATION),
        )
    }

    fun select(
        statement: Statement,
        routeId: RouteId? = null,
        directionId: DirectionId? = null,
    ): List<Direction> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_DIRECTION ")
            append("JOIN ${RouteSQL.T_ROUTE_IDS} ON $T_DIRECTION.${T_DIRECTION} = ${RouteSQL.T_ROUTE_IDS}.${RouteSQL.T_ROUTE_K_ID_INT} ")
            routeId?.let {
                append("WHERE ${RouteSQL.T_ROUTE_IDS}.${RouteSQL.T_ROUTE_IDS_K_ID} = '$it'")
            }
            directionId?.let {
                append("WHERE ${T_DIRECTION}.${T_DIRECTION_K_DIRECTION_ID} = $it")
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
}