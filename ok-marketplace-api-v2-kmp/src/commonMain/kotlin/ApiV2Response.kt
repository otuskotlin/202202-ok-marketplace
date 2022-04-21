package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.SerializationException
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.reflect.KClass

fun apiV2ResponseSerialize(response: IResponse): String = when (response) {
    is AdCreateResponse -> serializationMapper.encodeToString(ResponseSerializers.create, response)
    is AdReadResponse -> serializationMapper.encodeToString(ResponseSerializers.read, response)
    is AdUpdateResponse -> serializationMapper.encodeToString(ResponseSerializers.update, response)
    is AdDeleteResponse -> serializationMapper.encodeToString(ResponseSerializers.delete, response)
    is AdSearchResponse -> serializationMapper.encodeToString(ResponseSerializers.search, response)
    is AdOffersResponse -> serializationMapper.encodeToString(ResponseSerializers.offers, response)

    else -> throw SerializationException("Unknown API class to serialize: ${response::class} in ${::apiV2ResponseSerialize::class}")
}

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserializeTyped(json: String, clazz: KClass<T>): T = when (clazz) {
    IResponse::class -> serializationMapper.decodeFromString(AdResponseSerializer, json) as T

    AdCreateResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.create, json) as T
    AdReadResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.read, json) as T
    AdUpdateResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.update, json) as T
    AdDeleteResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.delete, json) as T
    AdSearchResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.search, json) as T
    AdOffersResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.offers, json) as T

    else -> throw SerializationException("Unknown API class to serialize: $clazz in apiV2ResponseDeserializeTyped")
}

inline fun <reified T : IResponse> apiV2ResponseDeserialize(json: String): T = apiV2ResponseDeserializeTyped(json, T::class)
