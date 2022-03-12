package org.mtransit.commons

fun Long.toIntTimestampSec(): Int {
    // TODO later max integer = 2147483647 = Tuesday, January 19, 2038 3:14:07 AM GMT
    return this.toInt().takeIf { it > 0 } ?: throw Exception("Invalid timestamp in sec '$this'!")
}