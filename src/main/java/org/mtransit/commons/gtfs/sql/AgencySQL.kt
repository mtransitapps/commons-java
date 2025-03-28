package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.gtfs.data.Agency
import org.mtransit.commons.gtfs.data.AgencyId
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import java.sql.ResultSet
import java.sql.Statement

object AgencySQL : CommonSQL<Agency>(), TableSQL {

    const val T_AGENCY_IDS = "agency_id"
    const val T_AGENCY_IDS_K_ID_INT = "agency_id_int"
    const val T_AGENCY_IDS_K_ID = "agency_id"

    override fun getIdsTable() = SQLTableDef.makeIdsTable(
        tableName = T_AGENCY_IDS,
        columnNameIdInt = T_AGENCY_IDS_K_ID_INT,
        columnNameId = T_AGENCY_IDS_K_ID,
    )

    const val T_AGENCY = "agency"

    const val T_AGENCY_K_ID_INT = "agency_id_int"
    const val T_AGENCY_K_AGENCY_NAME = "agency_name"
    const val T_AGENCY_K_URL = "agency_url"
    const val T_AGENCY_K_TIMEZONE = "agency_timezone"
    const val T_AGENCY_K_LANG = "agency_lang"
    const val T_AGENCY_K_PHONE = "agency_phone"
    const val T_AGENCY_K_FARE_URL = "agency_fare_url"
    const val T_AGENCY_K_EMAIL = "agency_email"

    override fun getMainTable() = SQLTableDef(
        T_AGENCY,
        listOf(
            SQLColumDef(T_AGENCY_K_ID_INT, SQLUtils.INT, primaryKey = true, foreignKey = SQLForeignKey(T_AGENCY_IDS, T_AGENCY_IDS_K_ID_INT)),
            SQLColumDef(T_AGENCY_K_AGENCY_NAME, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_URL, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_TIMEZONE, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_LANG, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_PHONE, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_FARE_URL, SQLUtils.TXT),
            SQLColumDef(T_AGENCY_K_EMAIL, SQLUtils.TXT),
        ),
        insertAllowReplace = false,
    )

    override fun toInsertColumns(statement: Statement, mainObject: Agency) = with(mainObject) {
        arrayOf<Any?>(
            getOrInsertIdInt(statement, agencyId),
            agencyName.quotesEscape(),
            agencyUrl.quotesEscape(),
            agencyTimezone.quotesEscape(),
            agencyLang?.quotesEscape(),
            agencyPhone?.quotesEscape(),
            agencyFareUrl?.quotesEscape(),
            agencyEmail?.quotesEscape()
        )
    }

    fun select(agencyId: AgencyId? = null, statement: Statement): List<Agency> {
        val sql = buildString {
            append("SELECT ")
            append("* ")
            append("FROM $T_AGENCY ")
            append("JOIN $T_AGENCY_IDS ON $T_AGENCY.$T_AGENCY_K_ID_INT = $T_AGENCY_IDS.$T_AGENCY_K_ID_INT ")
            agencyId?.let {
                append("WHERE $T_AGENCY_IDS.$T_AGENCY_IDS_K_ID = '$it'")
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
        Agency(
            agencyId = getString(T_AGENCY_IDS_K_ID),
            agencyName = getString(T_AGENCY_K_AGENCY_NAME),
            agencyUrl = getString(T_AGENCY_K_URL),
            agencyTimezone = getString(T_AGENCY_K_TIMEZONE),
            agencyLang = getString(T_AGENCY_K_LANG),
            agencyPhone = getString(T_AGENCY_K_PHONE),
            agencyFareUrl = getString(T_AGENCY_K_FARE_URL),
            agencyEmail = getString(T_AGENCY_K_EMAIL)
        )
    }
}