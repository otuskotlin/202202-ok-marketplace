package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.json.*

internal val serializationMapper = Json {
    encodeDefaults = true
}
