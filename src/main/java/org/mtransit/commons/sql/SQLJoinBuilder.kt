package org.mtransit.commons.sql

@Suppress("unused")
class SQLJoinBuilder private constructor(table: String) {

    companion object {
        @JvmStatic
        fun getNew(table: String): SQLJoinBuilder {
            return SQLJoinBuilder(table)
        }
    }

    private val sqlJoinSb: StringBuilder = StringBuilder(table)

    fun innerJoin(table: String, table1: String, column1: String, table2: String, column2: String): SQLJoinBuilder {
        sqlJoinSb.append(SQLUtils.INNER_JOIN).append(table).append(SQLUtils.ON) //
            .append(SQLUtils.getTableColumn(table1, column1)) //
            .append(SQLUtils.EQ) //
            .append(SQLUtils.getTableColumn(table2, column2))
        return this
    }

    fun build(): String {
        return sqlJoinSb.toString()
    }
}