package org.mtransit.commons.model

/**
 * @see kotlin.Triple()
 */
data class Quintuple<out A, out B, out C, out D, out E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E,
) : java.io.Serializable { // kotlin.io.Serializable is internal

    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"
}