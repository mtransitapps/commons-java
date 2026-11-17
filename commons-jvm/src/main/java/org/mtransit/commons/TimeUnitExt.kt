package org.mtransit.commons

import java.util.Date
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun Long.secToMs() = this.seconds.inWholeMilliseconds

fun Int.secToMs() = this.seconds.inWholeMilliseconds

fun Long.msToSec() = this.milliseconds.inWholeSeconds

fun Long.daysToMs() = this.days.inWholeMilliseconds

fun Int.daysToMs() = this.days.inWholeMilliseconds

fun Long.toDate() = Date(this)
