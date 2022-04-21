package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.json.*

/**
 * Объект настроенного kotlinx json-мапера
 */
internal val serializationMapper = Json {
    encodeDefaults = true
}
