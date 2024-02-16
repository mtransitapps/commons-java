package org.mtransit.commons.sql

import org.mtransit.commons.sql.SQLUtils.SQL_NULL
import java.sql.ResultSet

fun Boolean.toSQL(): Int {
    return SQLUtils.toSQLBoolean(this)
}

fun Int.fromSQL(): Boolean {
    return SQLUtils.fromSQLBoolean(this)
}

fun ResultSet.getStringOrNull(columnLabel: String) = this.getString(columnLabel).takeIf { it != SQL_NULL }?.takeIf { it.isNotBlank() }
