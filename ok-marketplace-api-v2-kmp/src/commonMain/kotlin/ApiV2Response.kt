package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse

fun apiV2ResponseSerialize(Response: IResponse): String = serializationMapper.encodeToString(AdResponseSerializer, Response)

@Suppress("UNCHECKED_CAST")
fun <T : Any> apiV2ResponseDeserialize(json: String): T = serializationMapper.decodeFromString(AdResponseSerializer, json) as T
