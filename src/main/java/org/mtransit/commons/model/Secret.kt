package org.mtransit.commons.model

/**
 * Hide "secret" data from toString()
 */
data class Secret<T>(val data: T) {
    override fun toString() = "Secret(data=***)"
}
