package org.mtransit.commons.sql

@Suppress("unused", "MemberVisibilityCanBePrivate")
class SQLInsertBuilder private constructor(table: String, allowReplace: Boolean = false) {

    companion object {
        @JvmOverloads
        @JvmStatic
        fun getNew(table: String, allowReplace: Boolean = false): SQLInsertBuilder {
            return SQLInsertBuilder(table, allowReplace)
        }

        fun compile(
            sqlInjectQuery: String,
            vararg values: Any?
        ) = String.format(sqlInjectQuery, values.asList().joinToString(","))
    }

    private val sqlInsertSb: StringBuilder = StringBuilder(
        if (allowReplace) SQLUtils.INSERT_OR_REPLACE_INTO else SQLUtils.INSERT_INTO
    ).append(table).append(SQLUtils.P1)

    private var nbColumn = 0

    fun appendColumn(name: String): SQLInsertBuilder {
        if (nbColumn > 0) {
            sqlInsertSb.append(SQLUtils.COLUMN_SEPARATOR)
        }
        sqlInsertSb.append(name)
        nbColumn++
        return this
    }

    fun appendColumns(vararg insertColumns: String): SQLInsertBuilder {
        for (insertColumn in insertColumns) {
            appendColumn(insertColumn)
        }
        return this
    }

    fun build(): String {
        return sqlInsertSb.append(SQLUtils.INSERT_INTO_VALUES).toString()
    }
}