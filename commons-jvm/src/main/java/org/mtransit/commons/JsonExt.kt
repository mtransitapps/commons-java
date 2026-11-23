package org.mtransit.commons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Enum<*> -> JsonPrimitive(this.name)
    is Map<*, *> -> JsonObject(
        this.entries.associate { (k, v) -> k.toString() to v.toJsonElement() }
    )

    is Iterable<*> -> JsonArray(this.map { it.toJsonElement() })
    is Array<*> -> JsonArray(this.map { it.toJsonElement() })
    else -> throw IllegalArgumentException("Unsupported type for JsonElement: ${this::class}")
}

fun Any?.toJsonPrimitive(): JsonPrimitive = when (this) {
    null -> JsonNull
    is JsonPrimitive -> this
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is String -> JsonPrimitive(this)
    is Enum<*> -> JsonPrimitive(this.name)
    else -> throw IllegalArgumentException("Cannot convert ${this::class} to JsonPrimitive")
}

fun JsonElement.toAny(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> when {
        isString -> content
        booleanOrNull != null -> boolean
        longOrNull != null -> long
        doubleOrNull != null -> double
        else -> content
    }
    is JsonObject -> mapValues { it.value.toAny() }
    is JsonArray -> map { it.toAny() }
}

inline fun JsonObject.edit(builderAction: JsonObjectBuilder.() -> Unit): JsonObject {
    return buildJsonObject {
        forEach { (key, value) -> put(key, value) }
        builderAction()
    }
}
