package org.mtransit.commons

import java.util.Calendar
import java.util.TimeZone

var Calendar.hourOfTheDay: Int
    get() = this[Calendar.HOUR_OF_DAY]
    set(value) {
        set(Calendar.HOUR_OF_DAY, value)
    }

var Calendar.date: Int
    get() = this[Calendar.DATE]
    set(value) {
        set(Calendar.DATE, value)
    }

fun Long.isSameDay(other: Long, timeZone: TimeZone): Boolean {
    return this.toCalendar(timeZone).isSameDay(other.toCalendar(timeZone))
}

fun Calendar.isSameDay(other: Calendar): Boolean {
    return this[Calendar.ERA] == other[Calendar.ERA]
            && this[Calendar.YEAR] == other[Calendar.YEAR]
            && this[Calendar.DAY_OF_YEAR] == other[Calendar.DAY_OF_YEAR]

}

val Calendar.beginningOfDay: Calendar
    get() = (this.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

fun Long.toCalendar()= Calendar.getInstance().apply { timeInMillis = this@toCalendar }

fun Long.toCalendar(timeZone: TimeZone)= Calendar.getInstance(timeZone).apply { timeInMillis = this@toCalendar }
