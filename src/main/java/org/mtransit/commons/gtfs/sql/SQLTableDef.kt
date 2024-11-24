package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.sql.SQLCreateBuilder
import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils

data class SQLTableDef(
    val tableName: String,
    val columns: List<SQLColumDef>,
    val insertAllowReplace: Boolean = false,
) {

    fun getSQLCreateTableQuery() = SQLCreateBuilder.getNew(tableName).apply {
        columns.forEach { columnDef ->
            appendColumn(columnDef.columnName, columnDef.columnType)
        }
        columns.filter { it.primaryKey }.forEach { columnDef ->
            appendPrimaryKeys(columnDef.columnName)
        }
        columns.forEach { columnDef ->
            columnDef.foreignKey?.let { foreignKey ->
                appendForeignKey(
                    columnName = columnDef.columnName,
                    fkTable = foreignKey.foreignKeyColumn,
                    fkColumn = foreignKey.foreignKeyTable
                )
            }
        }
    }.build()

    fun getSQLInsertTableQuery() = SQLInsertBuilder.getNew(tableName, insertAllowReplace).apply {
        columns.forEach { columnDef ->
            if (columnDef.columnType == SQLUtils.INT_PK_AUTO) {
                return@forEach // SKIP AUTOINCREMENT
            }
            appendColumn(columnDef.columnName)
        }
    }.build()

    fun getSQLDropIfExistsQuery() = SQLUtils.getSQLDropIfExistsQuery(tableName)

    companion object {
        fun makeIdsTable(
            tableName: String,
            columnNameIdInt: String = "${tableName}_id_int",
            columnNameId: String = "${tableName}_id",
        ) = SQLTableDef(
            tableName, listOf(
                SQLColumDef(columnNameIdInt, SQLUtils.INT_PK_AUTO),
                SQLColumDef(columnNameId, SQLUtils.TXT),
            )
        )

        fun makeIdsTableSelect(tableDef: SQLTableDef, id: String): String? {
            val idIntColumn = tableDef.columns.singleOrNull { it.columnType == SQLUtils.INT_PK_AUTO }?.columnName
                ?: throw IllegalArgumentException("ID INT column not found!")
            val idColumn = tableDef.columns.singleOrNull { it.columnType == SQLUtils.TXT }?.columnName
                ?: throw IllegalArgumentException("ID column not found!")
            return "SELECT $idIntColumn FROM ${tableDef.tableName} WHERE $idColumn = '$id'"
        }
    }
}