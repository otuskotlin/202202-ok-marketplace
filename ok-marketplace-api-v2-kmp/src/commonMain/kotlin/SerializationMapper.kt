package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.reflect.KClass

internal val serializationMapper = Json {
    encodeDefaults = true
}

fun apiV2Serialize(obj: Any): String = when(obj) {
//    is IRequest -> serializationMapper.encodeToString(PolymorphicSerializer(IRequest::class), obj)
    is AdCreateRequest -> serializationMapper.encodeToString(RequestSerializers.create, obj)
    is AdReadRequest   -> serializationMapper.encodeToString(RequestSerializers.read, obj)
    is AdUpdateRequest -> serializationMapper.encodeToString(RequestSerializers.update, obj)
    is AdDeleteRequest -> serializationMapper.encodeToString(RequestSerializers.delete, obj)
    is AdSearchRequest -> serializationMapper.encodeToString(RequestSerializers.search, obj)
    is AdOffersRequest -> serializationMapper.encodeToString(RequestSerializers.offers, obj)

    is AdCreateResponse -> serializationMapper.encodeToString(ResponseSerializers.create, obj)
    is AdReadResponse   -> serializationMapper.encodeToString(ResponseSerializers.read, obj)
    is AdUpdateResponse -> serializationMapper.encodeToString(ResponseSerializers.update, obj)
    is AdDeleteResponse -> serializationMapper.encodeToString(ResponseSerializers.delete, obj)
    is AdSearchResponse -> serializationMapper.encodeToString(ResponseSerializers.search, obj)
    is AdOffersResponse -> serializationMapper.encodeToString(ResponseSerializers.offers, obj)

    else -> throw SerializationException("Unknown API class to serialize: ${obj::class}")
}

fun <T : Any> apiV2DeserializeTyped(json: String, clazz: KClass<T>): T = when(clazz) {
    IRequest::class        -> serializationMapper.decodeFromString(AdRequestSerializer, json) as T
    IResponse::class       -> serializationMapper.decodeFromString(AdResponseSerializer, json) as T

    AdCreateRequest::class -> serializationMapper.decodeFromString(RequestSerializers.create, json) as T
    AdReadRequest::class   -> serializationMapper.decodeFromString(RequestSerializers.read, json) as T
    AdUpdateRequest::class -> serializationMapper.decodeFromString(RequestSerializers.update, json) as T
    AdDeleteRequest::class -> serializationMapper.decodeFromString(RequestSerializers.delete, json) as T
    AdSearchRequest::class -> serializationMapper.decodeFromString(RequestSerializers.search, json) as T
    AdOffersRequest::class -> serializationMapper.decodeFromString(RequestSerializers.offers, json) as T

    AdCreateResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.create, json) as T
    AdReadResponse::class   -> serializationMapper.decodeFromString(ResponseSerializers.read, json) as T
    AdUpdateResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.update, json) as T
    AdDeleteResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.delete, json) as T
    AdSearchResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.search, json) as T
    AdOffersResponse::class -> serializationMapper.decodeFromString(ResponseSerializers.offers, json) as T

    else -> throw SerializationException("Unknown API class to serialize: $clazz")
}

inline fun  <reified T: Any> apiV2Deserialize(json: String): T = apiV2DeserializeTyped(json, T::class)


