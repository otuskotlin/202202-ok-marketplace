package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.SerializationException
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.reflect.KClass

fun apiV2RequestSerialize(request: IRequest): String = when(request) {
    is AdCreateRequest -> serializationMapper.encodeToString(RequestSerializers.create, request)
    is AdReadRequest -> serializationMapper.encodeToString(RequestSerializers.read, request)
    is AdUpdateRequest -> serializationMapper.encodeToString(RequestSerializers.update, request)
    is AdDeleteRequest -> serializationMapper.encodeToString(RequestSerializers.delete, request)
    is AdSearchRequest -> serializationMapper.encodeToString(RequestSerializers.search, request)
    is AdOffersRequest -> serializationMapper.encodeToString(RequestSerializers.offers, request)
    else -> throw SerializationException("Unknown API class to serialize: ${request::class} in apiV2RequestSerialize")
}

fun <T : Any> apiV2RequestDeserializeTyped(json: String, clazz: KClass<T>): T = when(clazz) {
    IRequest::class        -> serializationMapper.decodeFromString(AdRequestSerializer, json) as T

    AdCreateRequest::class -> serializationMapper.decodeFromString(RequestSerializers.create, json) as T
    AdReadRequest::class   -> serializationMapper.decodeFromString(RequestSerializers.read, json) as T
    AdUpdateRequest::class -> serializationMapper.decodeFromString(RequestSerializers.update, json) as T
    AdDeleteRequest::class -> serializationMapper.decodeFromString(RequestSerializers.delete, json) as T
    AdSearchRequest::class -> serializationMapper.decodeFromString(RequestSerializers.search, json) as T
    AdOffersRequest::class -> serializationMapper.decodeFromString(RequestSerializers.offers, json) as T

    else -> throw SerializationException("Unknown API class to serialize: $clazz in apiV2RequestDeserializeTyped")
}

inline fun  <reified T: Any> apiV2RequestDeserialize(json: String): T = apiV2RequestDeserializeTyped(json, T::class)
