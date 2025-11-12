package org.mtransit.commons.gtfs.data

import org.mtransit.commons.GTFSCommons

// https://gtfs.org/documentation/schedule/reference/#calendar_datestxt
enum class CalendarExceptionType(val id: Int) {

    DEFAULT(GTFSCommons.EXCEPTION_TYPE_DEFAULT), // added by MT
    ADDED(GTFSCommons.EXCEPTION_TYPE_ADDED),
    REMOVED(GTFSCommons.EXCEPTION_TYPE_REMOVED),
    ;

    companion object {
        fun parse(id: Int): CalendarExceptionType {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}