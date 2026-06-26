package org.mtransit.commons.model

/**
 * Hide "secret" data from toString()
 */
data class Secret<out T>(val data: T) {
    override fun toString() = "Secret(data=***)"
}
