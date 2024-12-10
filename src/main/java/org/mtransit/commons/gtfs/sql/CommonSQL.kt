package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils.quotesEscape
import org.mtransit.commons.sql.executeQueryMT
import org.mtransit.commons.sql.executeUpdateMT
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

    open fun getSQLSelectIdIntFromId(id: String) = getIdsTable()?.let {
        SQLTableDef.makeIdsTableSelect(it, id)
    }

    private val cachedIds = mutableMapOf<String, Int>()

    override fun clearCache() {
        this.cachedIds.clear()
    }

    open fun getOrInsertIdInt(statement: Statement, id: String): Int {
        this.cachedIds[id]?.let {
            if (this is TripSQL) {
                throw Exception("Re-using trip id $id")
            }
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

    fun getMainTableSQLInsert() = getMainTable()?.getSQLInsertTableQuery()

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

    abstract fun toInsertColumns(statement: Statement, main: MainType): Array<Any?>

    open fun getSQLInsertOrReplace(statement: Statement, main: MainType) = getMainTableSQLInsert()?.let {
        SQLInsertBuilder.compile(
            it,
            *toInsertColumns(statement, main)
        )
    }

    abstract fun fromResultSet(rs: ResultSet): MainType

    fun getAlias(sourceColumn: String, valueColumn: String) = "${sourceColumn}_$valueColumn"
    fun getJoinAlias(sourceColumn: String) = "the_${sourceColumn}"
}