package org.mtransit.commons

import java.util.concurrent.TimeUnit

fun Long.secToMs() = TimeUnit.SECONDS.toMillis(this)

fun Int.secToMs() = this.toLong().secToMs()

fun Long.msToSec() = TimeUnit.MILLISECONDS.toSeconds(this)

fun Long.daysToMs() = TimeUnit.DAYS.toMillis(this)

fun Int.daysToMs() = this.toLong().daysToMs()
