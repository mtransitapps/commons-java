package org.mtransit.commons

fun Long.toIntOrNull(): Int? =
    this.takeIf { it in Int.MIN_VALUE..Int.MAX_VALUE }?.toInt()
