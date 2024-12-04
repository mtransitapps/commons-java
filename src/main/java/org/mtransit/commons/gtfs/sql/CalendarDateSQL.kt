package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.CalendarDate
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

    const val T_CALENDAR_DATES = "calendar_dates"

    const val T_CALENDAR_K_ID_INT = "service_id_int"
    const val T_CALENDAR_K_DATE = "date"
    const val T_CALENDAR_K_EXCEPTION_TYPE = "exception_type" // 1: added, 2: removed (MT: +0: default)

    override fun getMainTable() = SQLTableDef(
        T_CALENDAR_DATES,
        listOf(
            SQLColumDef(T_CALENDAR_K_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(T_SERVICE_IDS, T_SERVICE_IDS_K_ID_INT)),
            SQLColumDef(T_CALENDAR_K_DATE, SQLUtils.INT, primaryKey = true), // YYYYMMDD
            SQLColumDef(T_CALENDAR_K_EXCEPTION_TYPE, SQLUtils.INT), // 1: added, 2: removed (MT: +0: default)
        ),
        insertAllowReplace = true,
    )

    override fun toInsertColumns(statement: Statement, calendar: CalendarDate) = with(calendar) {
        arrayOf<Any?>(
            getOrInsertIdInt(statement, calendar.serviceId),
            date,
            exceptionType.id,
        )
    }

    fun insert(calendar: CalendarDate, statement: Statement): Boolean {
        return statement.executeUpdate(
            getSQLInsertOrReplace(
                statement,
                calendar
            )
        ) > 0
    }

    fun select(agencyId: ServiceId? = null, statement: Statement): List<CalendarDate> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_CALENDAR_DATES ")
            append("LEFT JOIN $T_SERVICE_IDS ON $T_CALENDAR_DATES.$T_CALENDAR_K_ID_INT = $T_SERVICE_IDS.$T_CALENDAR_K_ID_INT ")
            agencyId?.let {
                append("WHERE $T_SERVICE_IDS.$T_SERVICE_IDS_K_ID = '$it'")
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
            date = getInt(T_CALENDAR_K_DATE),
            exceptionTypeInt = getInt(T_CALENDAR_K_EXCEPTION_TYPE),
        )
    }

    fun delete(statement: Statement): Int {
        val sql = buildString {
            append("DELETE FROM $T_CALENDAR_DATES")
        }
        return statement.executeUpdate(sql)
    }
}