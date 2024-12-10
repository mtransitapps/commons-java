package org.mtransit.commons.sql

import org.mtransit.commons.sql.SQLUtils.SQL_NULL
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

fun Boolean.toSQL(): Int {
    return SQLUtils.toSQLBoolean(this)
}

fun Int.fromSQL(): Boolean {
    return SQLUtils.fromSQLBoolean(this)
}

fun ResultSet.getStringOrNull(columnLabel: String) = this.getString(columnLabel).takeIf { it != SQL_NULL }?.takeIf { it.isNotBlank() }

private const val LOG_SQL = false
// private const val LOG_SQL = true // DEBUG

fun Statement.executeUpdateMT(query: String?, returnGeneratedKeys: Boolean = false): Int {
    if (LOG_SQL) {
        println("SQL > $query.")
    }
    try {
        return this.executeUpdate(query, if (returnGeneratedKeys) Statement.RETURN_GENERATED_KEYS else Statement.NO_GENERATED_KEYS)
    } catch (e: SQLException) {
        throw Exception("SQL error while executing update '$query'!", e)
    }
}

fun Statement.executeQueryMT(query: String?): ResultSet {
    if (LOG_SQL) {
        println("SQL > $query.")
    }
    try {
        return this.executeQuery(query)
    } catch (e: SQLException) {
        throw Exception("SQL error while executing query '$query'!", e)
    }
}