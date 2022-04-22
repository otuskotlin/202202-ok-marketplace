package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest

fun apiV2RequestSerialize(request: IRequest): String = serializationMapper.encodeToString(AdRequestSerializer, request)

fun <T : Any> apiV2RequestDeserialize(json: String): T = serializationMapper.decodeFromString(AdRequestSerializer, json) as T
