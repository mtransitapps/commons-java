package org.mtransit.commons.model

/**
 * @see kotlin.Triple()
 */
data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
) : java.io.Serializable { // kotlin.io.Serializable is internal

    override fun toString(): String = "($first, $second, $third, $fourth)"
}