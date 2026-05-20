package org.mtransit.commons

import kotlinx.datetime.DatePeriod

val DatePeriod.weeks: Int get() = this.days
    .takeIf { this.months == 0 && it.mod(7) == 0 }?.div(7) // only if full weeks
    ?: 0
