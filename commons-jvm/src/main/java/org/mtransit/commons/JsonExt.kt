@file:Suppress("unused")

package org.mtransit.commons

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

@Suppress("CyclomaticComplexMethod")
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
    is ByteArray -> JsonArray(this.map { it.toJsonElement() })
    is ShortArray -> JsonArray(this.map { it.toJsonElement() })
    is IntArray -> JsonArray(this.map { it.toJsonElement() })
    is LongArray -> JsonArray(this.map { it.toJsonElement() })
    is FloatArray -> JsonArray(this.map { it.toJsonElement() })
    is DoubleArray -> JsonArray(this.map { it.toJsonElement() })
    is BooleanArray -> JsonArray(this.map { it.toJsonElement() })
    is CharArray -> JsonArray(this.map { it.toJsonElement() })
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

val JsonElement.jsonPrimitiveOrNull get() = this as? JsonPrimitive
val JsonElement.jsonObjectOrNull get() = this as? JsonObject
val JsonElement.jsonArrayOrNull get() = this as? JsonArray
val JsonElement.jsonNullOrNull get() = this as? JsonNull

fun JsonElement.toAny(): Any? = when (this) {
    is JsonNull -> null
    is JsonPrimitive -> if (isString) {
        content
    } else {
        booleanOrNull ?: longOrNull ?: doubleOrNull ?: content
    }

    is JsonObject -> mapValues { it.value.toAny() }
    is JsonArray -> map { it.toAny() }
}

inline fun JsonObject.copy(builderAction: JsonObjectBuilder.() -> Unit): JsonObject {
    return buildJsonObject {
        forEach { (key, value) -> put(key, value) }
        builderAction()
    }
}
