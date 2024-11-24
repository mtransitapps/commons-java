package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Agency
import org.mtransit.commons.gtfs.data.AgencyId
import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.Statement

object AgencySQL {

    const val T_AGENCY_IDS = "agency_ids"
    const val T_AGENCY_IDS_K_ID_INT = "agency_id_int"
    const val T_AGENCY_IDS_K_ID = "agency_id"

    @JvmStatic
    val T_AGENCY_IDS_SQL_CREATE = SQLCreateBuilder.getNew(T_AGENCY_IDS).apply {
        appendColumn(T_AGENCY_IDS_K_ID_INT, SQLUtils.INT_PK_AUTO)
        appendColumn(T_AGENCY_IDS_K_ID, SQLUtils.TXT)
    }.build()

    @JvmStatic
    val T_AGENCY_IDS_SQL_INSERT = SQLInsertBuilder.getNew(T_AGENCY_IDS).apply {
        // appendColumn(T_AGENCY_IDS_K_ID_INT) // AUTOINCREMENT
        appendColumn(T_AGENCY_IDS_K_ID)
    }.build()

    @JvmStatic
    val T_AGENCY_IDS_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_AGENCY_IDS)

    const val T_AGENCY = "agency"

    const val T_AGENCY_K_ID_INT = "agency_id_int"
    const val T_AGENCY_K_AGENCY_NAME = "agency_name"
    const val T_AGENCY_K_URL = "agency_url"
    const val T_AGENCY_K_TIMEZONE = "agency_timezone"
    const val T_AGENCY_K_LANG = "agency_lang"
    const val T_AGENCY_K_PHONE = "agency_phone"
    const val T_AGENCY_K_FARE_URL = "agency_fare_url"
    const val T_AGENCY_K_EMAIL = "agency_email"

    @JvmStatic
    val T_AGENCY_SQL_CREATE = SQLCreateBuilder.getNew(T_AGENCY).apply {
        appendColumn(T_AGENCY_K_ID_INT, SQLUtils.INT)
        appendColumn(T_AGENCY_K_AGENCY_NAME, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_URL, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_TIMEZONE, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_LANG, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_PHONE, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_FARE_URL, SQLUtils.TXT)
        appendColumn(T_AGENCY_K_EMAIL, SQLUtils.TXT)
            .appendPrimaryKeys(
                T_AGENCY_K_ID_INT,
            ).appendForeignKey(
                T_AGENCY_K_ID_INT, T_AGENCY_IDS, T_AGENCY_IDS_K_ID_INT
            )
    }.build()


    @JvmStatic
    val T_AGENCY_SQL_INSERT_OR_REPLACE = SQLInsertBuilder.getNew(T_AGENCY, allowReplace = true).apply {
        appendColumn(T_AGENCY_K_ID_INT)
        appendColumn(T_AGENCY_K_AGENCY_NAME)
        appendColumn(T_AGENCY_K_URL)
        appendColumn(T_AGENCY_K_TIMEZONE)
        appendColumn(T_AGENCY_K_LANG)
        appendColumn(T_AGENCY_K_PHONE)
        appendColumn(T_AGENCY_K_FARE_URL)
        appendColumn(T_AGENCY_K_EMAIL)
    }.build()

    @JvmStatic
    val T_AGENCY_SQL_DROP = SQLUtils.getSQLDropIfExistsQuery(T_AGENCY)

    fun getSQLCreateTablesQueries() = listOf(T_AGENCY_IDS_SQL_CREATE, T_AGENCY_SQL_CREATE)

    fun getSQLDropIfExistsQueries() = listOf(T_AGENCY_IDS_SQL_DROP, T_AGENCY_SQL_DROP)

    private fun getSQLInsertIds(agencyId: AgencyId) = SQLInsertBuilder.compile(
        T_AGENCY_IDS_SQL_INSERT,
        agencyId.quotesEscape()
    )

    private fun getSQLSelectIdIntFromId(agencyId: AgencyId) =
        "SELECT $T_AGENCY_IDS_K_ID_INT FROM $T_AGENCY_IDS WHERE $T_AGENCY_IDS_K_ID = '$agencyId'"

    private fun getSQLInsertOrReplace(agencyIdInt: Int, agency: Agency) = SQLInsertBuilder.compile(
        T_AGENCY_SQL_INSERT_OR_REPLACE,
        agencyIdInt,
        agency.agencyName.quotesEscape(),
        agency.agencyUrl.quotesEscape(),
        agency.agencyTimezone.quotesEscape(),
        agency.agencyLang?.quotesEscape(),
        agency.agencyPhone?.quotesEscape(),
        agency.agencyFareUrl?.quotesEscape(),
        agency.agencyEmail?.quotesEscape()
    )

    fun insert(agency: Agency, statement: Statement): Boolean {
        return statement.executeUpdate(
            getSQLInsertOrReplace(
                getOrInsertIdInt(statement, agency.agencyId),
                agency
            )
        ) > 0
    }

    fun getOrInsertIdInt(statement: Statement, agencyId: AgencyId): Int {
        val agencyIdInt = statement.executeQuery(getSQLSelectIdIntFromId(agencyId)).use { rs ->
            if (rs.next()) {
                rs.getInt(1)
            } else {
                if (statement.executeUpdate(getSQLInsertIds(agencyId)) > 0) {
                    statement.executeQuery(getSQLSelectIdIntFromId(agencyId)).use { rs2 ->
                        if (rs2.next()) {
                            rs2.getInt(1)
                        } else {
                            throw Exception("Error while inserting agency ID")
                        }
                    }
                } else {
                    throw Exception("Error while inserting agency ID")
                }
            }
        }
        return agencyIdInt
    }

    fun select(agencyId: AgencyId? = null, statement: Statement): List<Agency> {
        val sql = buildString {
            append("SELECT ")
            append(" * ")
            append("FROM $T_AGENCY ")
            append("LEFT JOIN $T_AGENCY_IDS ON $T_AGENCY.$T_AGENCY_K_ID_INT = $T_AGENCY_IDS.$T_AGENCY_K_ID_INT ")
            agencyId?.let {
                append("WHERE $T_AGENCY_IDS.$T_AGENCY_IDS_K_ID = '$it'")
            }
        }
        return statement.executeQuery(sql).use { rs ->
            val agencies = mutableListOf<Agency>()
            while (rs.next()) {
                agencies.add(
                    Agency(
                        agencyId = rs.getString(T_AGENCY_IDS_K_ID),
                        agencyName = rs.getString(T_AGENCY_K_AGENCY_NAME),
                        agencyUrl = rs.getString(T_AGENCY_K_URL),
                        agencyTimezone = rs.getString(T_AGENCY_K_TIMEZONE),
                        agencyLang = rs.getString(T_AGENCY_K_LANG),
                        agencyPhone = rs.getString(T_AGENCY_K_PHONE),
                        agencyFareUrl = rs.getString(T_AGENCY_K_FARE_URL),
                        agencyEmail = rs.getString(T_AGENCY_K_EMAIL)
                    )
                )
            }
            agencies
        }
    }

    fun count(statement: Statement): Int {
        val sql = "SELECT COUNT(*) AS count FROM $T_AGENCY"
        return statement.executeQuery(sql).use { rs ->
            if (rs.next()) {
                return rs.getInt("count")
            }
            throw Exception("Error while counting routes!")
        }
    }
}