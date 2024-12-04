package org.mtransit.commons.gtfs.data

typealias ServiceId = String

typealias Date = Int // YYYYMMDD

// Calendar is not used -> flatten to CalendarDate
data class CalendarDate(
    val serviceId: ServiceId,
    val date: Date, // YYYYMMDD
    val exceptionType: CalendarExceptionType,
) {
    constructor(
        serviceId: ServiceId,
        date: Date,
        exceptionTypeInt: Int
    ) : this(
        serviceId,
        date,
        CalendarExceptionType.parse(exceptionTypeInt)
    )
}
