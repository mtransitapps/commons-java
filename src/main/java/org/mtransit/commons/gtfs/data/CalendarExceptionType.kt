package org.mtransit.commons.gtfs.data

import org.mtransit.commons.GTFSCommons

enum class CalendarExceptionType(val id: Int) {
    DEFAULT(GTFSCommons.EXCEPTION_TYPE_DEFAULT),
    ADDED(GTFSCommons.EXCEPTION_TYPE_ADDED),
    REMOVED(GTFSCommons.EXCEPTION_TYPE_REMOVED),
    ;

    companion object {
        fun parse(id: Int): CalendarExceptionType {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}