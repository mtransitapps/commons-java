package org.mtransit.commons.gtfs.data

import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.ResultSet

// https://github.com/mbta/gtfs-documentation/blob/master/reference/gtfs.md#directionstxt
data class Direction(
    val routeId: RouteId,
    val directionId: DirectionId,
    val directionType: DirectionType?,
    val destination: Text?,
)

enum class DirectionType(val value: Text) {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West"),
    NORTH_EAST("Northeast"),
    NORTH_WEST("Northwest"),
    SOUTH_EAST("Southeast"),
    SOUTH_WEST("Southwest"),
    CLOCKWISE("Clockwise"),
    COUNTER_CLOCKWISE("Counterclockwise"),
    INBOUND("Inbound"),
    OUTBOUND("Outbound"),
    LOOP("Loop"),
    A_LOOP("A Loop"), // or "Loop A"? (not sure)
    B_LOOP("B Loop"), // or "Loop B"? (not sure)
    UNKNOWN("Unknown");

    fun toSQL() = this.takeIf { it != UNKNOWN }?.value?.quotesEscape()

    companion object {
        fun fromResultSet(rs: ResultSet, columnLabel: String) = fromValue(rs.getString(columnLabel))

        fun fromValue(value: String?): DirectionType {
            val valueLC = value?.lowercase()
            return entries.firstOrNull { it.value.lowercase() == valueLC } ?: UNKNOWN
        }
    }
}
