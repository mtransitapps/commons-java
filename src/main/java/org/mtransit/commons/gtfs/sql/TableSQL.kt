package org.mtransit.commons.gtfs.sql

interface TableSQL {
    fun getSQLCreateTablesQueries(): List<String>
    fun getSQLDropIfExistsQueries(): List<String>
    fun clearCache()
}