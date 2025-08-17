package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import org.mtransit.commons.sql.executeQueryMT
import org.mtransit.commons.sql.executeUpdateMT
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

abstract class CommonSQL<MainType>() : TableSQL {

    // region IDs table

    open fun getIdsTable(): SQLTableDef? = null

    fun getIdsTableSQLCreate() = getIdsTable()?.getSQLCreateTableQuery()

    fun getIdsTableSQLInsert() = getIdsTable()?.getSQLInsertTableQuery()

    fun getIdsTableSQLDrop() = getIdsTable()?.getSQLDropIfExistsQuery()

    open fun getSQLInsertIds(id: String) = getIdsTableSQLInsert()?.let { sql ->
        SQLInsertBuilder.compile(sql, id.quotesEscape())
    }

    @Suppress("unused")
    open fun getSQLSelectIdIntFromId(id: String) = getIdsTable()?.let {
        SQLTableDef.makeIdsTableSelect(it, id)
    }

    private val cachedIds = mutableMapOf<String, Int>()

    override fun clearCache() {
        this.cachedIds.clear()
    }

    open fun getOrInsertIdInt(statement: Statement, id: String): Int {
        this.cachedIds[id]?.let {
            return it
        }
        val update = statement.executeUpdateMT(getSQLInsertIds(id), returnGeneratedKeys = true)
        if (update <= 0) {
            throw Exception("Error while inserting $mainTableName ID $id!")
        }
        return statement.generatedKeys.use { rs2 ->
            if (rs2.next()) {
                rs2.getInt(1)
                    .also { this.cachedIds[id] = it }
            } else {
                throw Exception("Error while inserting $mainTableName ID $id!")
            }
        }
    }

    // endregion IDs table

    // region Main table

    open fun getMainTable(): SQLTableDef? = null

    private val mainTableName by lazy { getMainTable()?.tableName.orEmpty() }

    fun getMainTableSQLCreate() = getMainTable()?.getSQLCreateTableQuery()

    fun getMainTableSQLInsert(allowUpdate: Boolean = false) = getMainTable()?.getSQLInsertTableQuery(allowUpdate)

    fun getMainTableInsertPreparedStatement(allowUpdate: Boolean = false) = getMainTable()?.let {
        buildString {
            append((if (allowUpdate) SQLUtils.INSERT_OR_REPLACE_INTO else SQLUtils.INSERT_INTO))
            append(it.tableName)
            append(SQLUtils.VALUES_P1)
            it.columns.forEachIndexed { i, columnDef ->
                if (i > 0) {
                    append(SQLUtils.COLUMN_SEPARATOR)
                }
                append("?")
            }
            append(SQLUtils.P2)
        }
    }

    fun insertIntoMainTable(mainObject: MainType, statement: Statement, preparedStatement: PreparedStatement?, allowUpdate: Boolean = false): Boolean {
        preparedStatement?.apply {
            val mainTableColumns = getMainTable()?.columns ?: return false
            val columnsValues = toInsertColumns(statement, mainObject)
            columnsValues.forEachIndexed { i, columnValue ->
                val tableColum = mainTableColumns[i]
                try {
                    if (columnValue == null) {
                        setNull(i + 1, java.sql.Types.NULL)
                        return@forEachIndexed
                    }
                    when (tableColum.columnType) {
                        SQLUtils.INT -> setInt(i + 1, columnValue as Int)
                        SQLUtils.TXT -> setString(i + 1, columnValue as String)
                        else -> TODO("Unexpected column type for $tableColum!")
                    }
                } catch (e: Exception) {
                    System.err.println("Error while inserting '$columnValue' into '$mainTableName.${tableColum.columnName}' -${tableColum.columnType}!")
                    throw e
                }
            }
            addBatch()
            return true
        }
        return statement.executeUpdateMT(getSQLInsertOrReplace(statement, mainObject, allowUpdate)) > 0
    }

    fun getMainTableSQLDrop() = getMainTable()?.getSQLDropIfExistsQuery()

    open fun count(statement: Statement): Int {
        getMainTable()?.let { tableDef ->
            val sql = "SELECT COUNT(*) AS count FROM ${tableDef.tableName}"
            return statement.executeQueryMT(sql).use { rs ->
                if (rs.next()) {
                    return rs.getInt("count")
                }
                throw Exception("Error while counting routes!")
            }
        } ?: throw Exception("No main table!")
    }

    // endregion Main table

    override fun getSQLCreateTablesQueries() = buildList {
        getIdsTableSQLCreate()?.let { add(it) }
        getMainTableSQLCreate()?.let { add(it) }
    }

    override fun getSQLDropIfExistsQueries() = buildList {
        getIdsTableSQLDrop()?.let { add(it) }
        getMainTableSQLDrop()?.let { add(it) }
    }

    abstract fun toInsertColumns(statement: Statement, mainObject: MainType): Array<Any?>

    open fun getSQLInsertOrReplace(statement: Statement, mainObject: MainType, allowUpdate: Boolean = false) = getMainTableSQLInsert(allowUpdate)?.let {
        SQLInsertBuilder.compile(
            it,
            *toInsertColumns(statement, mainObject)
        )
    }

    abstract fun fromResultSet(rs: ResultSet): MainType

    fun getAlias(sourceColumn: String, valueColumn: String) = "${sourceColumn}_$valueColumn"
    fun getJoinAlias(sourceColumn: String) = "the_${sourceColumn}"
}