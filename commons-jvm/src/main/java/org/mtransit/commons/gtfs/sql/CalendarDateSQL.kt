package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.CalendarDate
import org.mtransit.commons.gtfs.data.CalendarExceptionType
import org.mtransit.commons.gtfs.data.ServiceId
import org.mtransit.commons.sql.SQLUtils
import java.sql.ResultSet
import java.sql.Statement

object CalendarDateSQL : CommonSQL<CalendarDate>(), TableSQL {

    const val T_SERVICE_IDS = "service_id"
    const val T_SERVICE_IDS_K_ID_INT = "service_id_int"
    const val T_SERVICE_IDS_K_ID = "service_id"

    override fun getIdsTable() = SQLTableDef.makeIdsTable(
        tableName = T_SERVICE_IDS,
        columnNameIdInt = T_SERVICE_IDS_K_ID_INT,
        columnNameId = T_SERVICE_IDS_K_ID,
    )

    fun selectServiceIds(statement: Statement): List<ServiceId> {
        val sql = "SELECT $T_SERVICE_IDS_K_ID FROM $T_SERVICE_IDS"
        return statement.executeQuery(sql).use { rs ->
            val routeIds = mutableListOf<String>()
            while (rs.next()) {
                routeIds.add(rs.getString(T_SERVICE_IDS_K_ID))
            }
            routeIds
        }
    }

    const val T_CALENDAR_DATE = "calendar_date"

    const val T_CALENDAR_DATE_K_SERVICE_ID_INT = "service_id_int"
    const val T_CALENDAR_DATE_K_DATE = "date"
    const val T_CALENDAR_DATE_K_EXCEPTION_TYPE = "exception_type" // 1: added, 2: removed (MT: +0: default)

    private const val CALENDAR_IN_CALENDAR_DATES = true

    override fun getMainTable() = SQLTableDef(
        T_CALENDAR_DATE,
        listOf(
            SQLColumDef(
                T_CALENDAR_DATE_K_SERVICE_ID_INT, SQLUtils.INT,
                primaryKey = !CALENDAR_IN_CALENDAR_DATES, foreignKey = SQLForeignKey(T_SERVICE_IDS, T_SERVICE_IDS_K_ID_INT)
            ),
            SQLColumDef(
                T_CALENDAR_DATE_K_DATE, SQLUtils.INT, // YYYYMMDD
                primaryKey = !CALENDAR_IN_CALENDAR_DATES
            ),
            SQLColumDef(T_CALENDAR_DATE_K_EXCEPTION_TYPE, SQLUtils.INT), // 1: added, 2: removed (MT: +0: default)
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, mainObject: CalendarDate) = with(mainObject) {
        arrayOf<Any?>(
            getOrInsertIdInt(statement, serviceId),
            date,
            exceptionType.id,
        )
    }

    fun select(
        serviceId: ServiceId? = null,
        exceptionTypes: Collection<CalendarExceptionType>? = null,
        statement: Statement,
    ): List<CalendarDate> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_CALENDAR_DATE ")
            append("JOIN $T_SERVICE_IDS ON $T_CALENDAR_DATE.$T_CALENDAR_DATE_K_SERVICE_ID_INT = $T_SERVICE_IDS.$T_CALENDAR_DATE_K_SERVICE_ID_INT ")
            serviceId?.let {
                append("WHERE $T_SERVICE_IDS.$T_SERVICE_IDS_K_ID = '$it'")
            }
            exceptionTypes?.takeIf { it.isNotEmpty() }?.let {
                append("WHERE $T_CALENDAR_DATE.$T_CALENDAR_DATE_K_EXCEPTION_TYPE IN (${it.joinToString { exceptionType -> "${exceptionType.id}" }})")
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
        CalendarDate(
            serviceId = getString(T_SERVICE_IDS_K_ID),
            date = getInt(T_CALENDAR_DATE_K_DATE),
            exceptionTypeInt = getInt(T_CALENDAR_DATE_K_EXCEPTION_TYPE),
        )
    }

    const val DELETE_CHUNK_SIZE = 1000

    fun delete(statement: Statement, calendarDates: Collection<CalendarDate>? = null): Int {
        if (calendarDates == null) {
            return statement.executeUpdate("DELETE FROM $T_CALENDAR_DATE")
        }
        var count = 0
        calendarDates.chunked(DELETE_CHUNK_SIZE).forEach { cCalendarDates ->
            val sql = buildString {
                append("DELETE FROM $T_CALENDAR_DATE")
                append(" WHERE ($T_CALENDAR_DATE_K_SERVICE_ID_INT,$T_CALENDAR_DATE_K_DATE,$T_CALENDAR_DATE_K_EXCEPTION_TYPE) IN (")
                append(
                    cCalendarDates.joinToString(separator = ",") { calendarDate ->
                        "(${getOrInsertIdInt(statement, calendarDate.serviceId)},${calendarDate.date},${calendarDate.exceptionType.id})"
                    }
                )
                append(")")
            }
            count += statement.executeUpdate(sql)
        }
        return count
    }
}
