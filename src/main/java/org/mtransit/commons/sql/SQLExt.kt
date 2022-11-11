package org.mtransit.commons.sql

fun Boolean.toSQL(): Int {
    return SQLUtils.toSQLBoolean(this)
}

fun Int.fromSQL(): Boolean {
    return SQLUtils.fromSQLBoolean(this)
}