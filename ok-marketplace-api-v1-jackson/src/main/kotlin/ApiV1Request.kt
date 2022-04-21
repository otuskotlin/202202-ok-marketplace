package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*

fun apiV1RequestSerialize(request: IRequest): String = jacksonMapper.writeValueAsString(request)

fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    jacksonMapper.readValue(json, IRequest::class.java) as T
