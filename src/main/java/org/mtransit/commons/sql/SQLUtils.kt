package org.mtransit.commons.sql

import org.mtransit.commons.Constants.EMPTY


@Suppress("MemberVisibilityCanBePrivate", "unused")
object SQLUtils {

    const val PRAGMA_AUTO_VACUUM_NONE = "PRAGMA auto_vacuum=NONE;"

    const val BASE_COLUMNS_ID = "_id"

    const val INT = " integer"
    const val INT_PK = "$INT PRIMARY KEY"
    const val INT_PK_AUTO = "$INT_PK AUTOINCREMENT"
    const val TXT = " text"
    const val REAL = " real"

    const val STRING_DELIMITER = "'"
    const val POINT = "."
    const val P2 = ")"
    const val P1 = "("
    const val PERCENT = "%"
    const val GT = ">"
    const val LT = "<"
    const val EQ = "="
    const val NE = "!="
    const val COLUMN_SEPARATOR = ","
    const val LIKE = " LIKE "
    const val ON = " ON "
    const val INNER_JOIN = " INNER JOIN "
    const val FOREIGN_KEY_REFERENCES = " REFERENCES "
    const val PRIMARY_KEY_ = "PRIMARY KEY "
    const val FOREIGN_KEY = " FOREIGN KEY "
    const val AS = " AS "
    const val ASC = " ASC"
    const val DESC = " DESC"
    const val NOT = " NOT "
    const val BETWEEN = " BETWEEN "
    const val AND = " AND "
    const val OR = " OR "
    const val IN = " IN "

    const val CREATE_TABLE = "CREATE TABLE "
    const val CREATE_TABLE_IF_NOT_EXIST = CREATE_TABLE + "IF NOT EXISTS "
    const val INSERT_INTO = "INSERT INTO "
    const val INSERT_OR_REPLACE_INTO = "INSERT OR REPLACE INTO "
    const val VALUES_P1 = " VALUES$P1"
    const val INSERT_INTO_VALUES = ")$VALUES_P1%s)"
    const val DROP_TABLE = "DROP TABLE "
    const val DROP_TABLE_IF_EXISTS = DROP_TABLE + "IF EXISTS "


    @JvmStatic
    fun getSQLForeignKey(columnName: String, fkTable: String, fkColumn: String): String {
        return FOREIGN_KEY + P1 + columnName + P2 + FOREIGN_KEY_REFERENCES + fkTable + P1 + fkColumn + P2
    }

    @JvmStatic
    fun getSQLPrimaryKeys(vararg columnNames: String): String {
        val sb = StringBuilder()
        if (columnNames.isNotEmpty()) {
            for (columnName in columnNames) {
                if (sb.isEmpty()) {
                    sb.append(PRIMARY_KEY_).append(P1)
                } else {
                    sb.append(COLUMN_SEPARATOR)
                }
                sb.append(columnName)
            }
            sb.append(P2)
        }
        return sb.toString()
    }

    @JvmStatic
    fun getSQLDropIfExistsQuery(table: String): String {
        return DROP_TABLE_IF_EXISTS + table
    }

    @JvmStatic
    fun getTableColumn(table: String, column: String): String {
        return table + POINT + column
    }

    @JvmStatic
    fun getLike(tableColumn: String, value: String): String {
        return tableColumn + LIKE + STRING_DELIMITER + PERCENT + value + PERCENT + STRING_DELIMITER
    }

    @JvmStatic
    fun getWhereGroup(andOr: String, vararg whereClauses: String): String {
        val sb = StringBuilder(P1)
        for (whereClause in whereClauses) {
            if (sb.isNotEmpty()) {
                sb.append(andOr)
            }
            sb.append(whereClause)
        }
        return sb.append(P2).toString()
    }

    @JvmStatic
    fun getBetween(tableColumn: String, value1: Any, value2: Any): String {
        return tableColumn + BETWEEN + value1 + AND + value2
    }

    @JvmStatic
    fun mergeSortOrder(vararg sortOrders: String): String {
        val sb = java.lang.StringBuilder()
        for (sortOrder in sortOrders) {
            if (sb.isNotEmpty()) {
                sb.append(COLUMN_SEPARATOR)
            }
            sb.append(sortOrder)
        }
        return sb.toString()
    }

    @JvmStatic
    fun getSortOrderAscending(column: String): String {
        return column + ASC
    }

    @JvmStatic
    fun getSortOrderDescending(column: String): String {
        return column + DESC
    }

    @JvmStatic
    fun getWhereEquals(column: String, value: Any): String {
        return column + EQ + value
    }

    @JvmStatic
    fun getWhereInferior(column: String, value: Any): String {
        return column + LT + value
    }

    @JvmStatic
    fun getWhereSuperior(column: String, value: Any): String {
        return column + GT + value
    }

    @JvmStatic
    fun getWhereEqualsString(column: String, value: String): String {
        return column + EQ + escapeString(value)
    }

    @JvmStatic
    fun getWhereIn(tableColumn: String, values: Collection<Any>?): String {
        val sb = StringBuilder(tableColumn).append(IN).append(P1)
        if (values != null) {
            for ((i, value) in values.withIndex()) {
                if (i > 0) {
                    sb.append(COLUMN_SEPARATOR)
                }
                sb.append(value)
            }
        }
        return sb.append(P2).toString()
    }

    @JvmStatic
    fun getWhereInString(tableColumn: String, values: Collection<String>?): String {
        val sb = StringBuilder(tableColumn).append(IN).append(P1)
        if (values != null) {
            for ((i, value) in values.withIndex()) {
                if (i > 0) {
                    sb.append(COLUMN_SEPARATOR)
                }
                sb.append(escapeString(value))
            }
        }
        return sb.append(P2).toString()
    }

    @JvmStatic
    fun escapeString(string: String): String {
        return STRING_DELIMITER + string + STRING_DELIMITER
    }

    const val BOOLEAN_TRUE = 1
    const val BOOLEAN_FALSE = 0

    @JvmStatic
    fun fromSQLBoolean(intValue: Int): Boolean {
        return intValue == BOOLEAN_TRUE
    }

    @JvmStatic
    fun toSQLBoolean(value: Boolean): Int {
        return if (value) BOOLEAN_TRUE else BOOLEAN_FALSE
    }

    @JvmStatic
    fun getWhereBooleanNotTrue(tableColumn: String): String {
        return tableColumn + NE + BOOLEAN_TRUE
    }

    @JvmStatic
    fun appendToSelection(selection: String?, append: String): String {
        return selection?.let { it + AND + append } ?: append
    }

    private const val CONCATENATE_SEPARATOR = "||"

    @JvmStatic
    fun concatenate(separator: String, vararg strings: String): String {
        val sb = StringBuilder()
        if (strings.isNotEmpty()) {
            for (string in strings) {
                if (sb.isNotEmpty()) {
                    sb.append(CONCATENATE_SEPARATOR).append(separator).append(CONCATENATE_SEPARATOR)
                }
                sb.append(string)
            }
        }
        return sb.toString()
    }
}