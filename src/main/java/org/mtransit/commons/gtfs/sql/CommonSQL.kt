package org.mtransit.commons.gtfs.sql

import org.mtransit.commons.sql.SQLInsertBuilder
import org.mtransit.commons.sql.SQLUtils.quotesEscape

abstract class CommonSQL<MainType>() : TableSQL {

    open fun getIdsTable(): SQLTableDef? = null

    fun getIdsTableSQLCreate() = getIdsTable()?.getSQLCreateTableQuery()

    fun getIdsTableSQLInsert() = getIdsTable()?.getIdsTableSQLInsert()

    fun getIdsTableSQLDrop() = getIdsTable()?.getSQLDropIfExistsQuery()

    open fun getSQLInsertIds(id: String) = getIdsTableSQLInsert()?.let {
        SQLInsertBuilder.compile(
            it,
            id.quotesEscape()
        )
    }

    open fun getSQLSelectIdIntFromId(id: String) = getIdsTable()?.let {
        SQLTableDef.makeIdsTableSelect(it, id)
    }

    open fun getMainTable(): SQLTableDef? = null

    fun getMainTableSQLCreate() = getMainTable()?.getSQLCreateTableQuery()

    fun getMainTableSQLInsert() = getMainTable()?.getIdsTableSQLInsert()

    fun getMainTableSQLDrop() = getMainTable()?.getSQLDropIfExistsQuery()

    override fun getSQLCreateTablesQueries() = buildList {
        getIdsTableSQLCreate()?.let { add(it) }
        getMainTableSQLCreate()?.let { add(it) }
    }

    override fun getSQLDropIfExistsQueries() = buildList {
        getIdsTableSQLDrop()?.let { add(it) }
        getMainTableSQLDrop()?.let { add(it) }
    }

    abstract fun toInsertColumns(idInt: Int, main: MainType): Array<Any?>

    open fun getSQLInsertOrReplace(idInt: Int, main: MainType) = getMainTableSQLInsert()?.let {
        SQLInsertBuilder.compile(
            it,
            *toInsertColumns(idInt, main)
        )
    }
}