package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils.quotesEscape
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

    open fun getOrInsertIdInt(statement: Statement, id: String): Int {
        return statement.executeQuery(getSQLSelectIdIntFromId(id)).use { rs ->
            if (rs.next()) {
                rs.getInt(1)
            } else {
                if (statement.executeUpdate(getSQLInsertIds(id)) > 0) {
                    statement.executeQuery(getSQLSelectIdIntFromId(id)).use { rs2 ->
                        if (rs2.next()) {
                            rs2.getInt(1)
                        } else {
                            throw Exception("Error while inserting agency ID")
                        }
                    }
                } else {
                    throw Exception("Error while inserting agency ID")
                }
            }
        }
    }

    // endregion IDs table

    // region Main table

    open fun getMainTable(): SQLTableDef? = null

    fun getMainTableSQLCreate() = getMainTable()?.getSQLCreateTableQuery()

    fun getMainTableSQLInsert() = getMainTable()?.getSQLInsertTableQuery()

    fun getMainTableSQLDrop() = getMainTable()?.getSQLDropIfExistsQuery()

    open fun count(statement: Statement): Int {
        getMainTable()?.let { tableDef ->
            val sql = "SELECT COUNT(*) AS count FROM ${tableDef.tableName}"
            return statement.executeQuery(sql).use { rs ->
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