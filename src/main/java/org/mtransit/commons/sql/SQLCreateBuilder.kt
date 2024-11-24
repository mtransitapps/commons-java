package org.mtransit.commons.sql

@Suppress("unused", "MemberVisibilityCanBePrivate")
class SQLCreateBuilder private constructor(table: String) {

    companion object {
        @JvmStatic
        fun getNew(table: String): SQLCreateBuilder {
            return SQLCreateBuilder(table)
        }
    }

    private val sqlCreateSb: StringBuilder = StringBuilder(SQLUtils.CREATE_TABLE_IF_NOT_EXIST).append(table).append(SQLUtils.P1)

    private var nbColumn = 0

    fun appendColumn(name: String, type: String): SQLCreateBuilder {
        if (nbColumn > 0) {
            sqlCreateSb.append(SQLUtils.COLUMN_SEPARATOR)
        }
        sqlCreateSb.append(name).append(type)
        nbColumn++
        return this
    }

    fun appendColumns(vararg createColumnNameAndTypes: Array<String>): SQLCreateBuilder {
        for (createLine in createColumnNameAndTypes) {
            appendColumn(createLine[0], createLine[1])
        }
        return this
    }

    fun appendForeignKey(columnName: String, fkTable: String, fkColumn: String): SQLCreateBuilder {
        if (nbColumn > 0) {
            sqlCreateSb.append(SQLUtils.COLUMN_SEPARATOR)
        }
        sqlCreateSb.append(SQLUtils.getSQLForeignKey(columnName, fkTable, fkColumn))
        nbColumn++
        return this
    }

    fun appendPrimaryKeys(vararg columnNames: String): SQLCreateBuilder {
        if (nbColumn > 0) {
            sqlCreateSb.append(SQLUtils.COLUMN_SEPARATOR)
        }
        sqlCreateSb.append(SQLUtils.getSQLPrimaryKeys(*columnNames))
        nbColumn++
        return this
    }

    fun build(): String {
        return sqlCreateSb.append(SQLUtils.P2).toString()
    }
}