package org.mtransit.commons.gtfs.sql

data class SQLColumDef(
    val columnName: String,
    val columnType: String,
    val primaryKey: Boolean = false,
    val foreignKey: SQLForeignKey? = null,
)

data class SQLForeignKey(
    val foreignKeyTable: String,
    val foreignKeyColumn: String,
)