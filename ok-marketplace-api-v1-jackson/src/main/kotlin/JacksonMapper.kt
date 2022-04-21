package ru.otus.otuskotlin.marketplace.api.v1

import com.fasterxml.jackson.databind.ObjectMapper

internal val jacksonMapper = ObjectMapper().apply {
//    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}
