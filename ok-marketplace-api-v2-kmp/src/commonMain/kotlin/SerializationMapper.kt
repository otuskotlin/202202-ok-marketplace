package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Объект настроенного kotlinx json-мапера
 */
@OptIn(ExperimentalSerializationApi::class)
internal val serializationMapper = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        polymorphicDefaultSerializer(IRequest::class) { instance ->
            @Suppress("UNCHECKED_CAST")
            when (instance) {
                is AdCreateRequest -> RequestSerializers.create as SerializationStrategy<IRequest>
                is AdReadRequest   -> RequestSerializers.read as SerializationStrategy<IRequest>
                is AdUpdateRequest -> RequestSerializers.update as SerializationStrategy<IRequest>
                is AdDeleteRequest -> RequestSerializers.delete as SerializationStrategy<IRequest>
                is AdSearchRequest -> RequestSerializers.search as SerializationStrategy<IRequest>
                is AdOffersRequest -> RequestSerializers.offers as SerializationStrategy<IRequest>
                else -> null
            }
        }
        polymorphicDefault(IRequest::class) {
            AdRequestSerializer
        }
        polymorphicDefaultSerializer(IResponse::class) { instance ->
            @Suppress("UNCHECKED_CAST")
            when (instance) {
                is AdCreateResponse -> ResponseSerializers.create as SerializationStrategy<IResponse>
                is AdReadResponse   -> ResponseSerializers.read as SerializationStrategy<IResponse>
                is AdUpdateResponse -> ResponseSerializers.update as SerializationStrategy<IResponse>
                is AdDeleteResponse -> ResponseSerializers.delete as SerializationStrategy<IResponse>
                is AdSearchResponse -> ResponseSerializers.search as SerializationStrategy<IResponse>
                is AdOffersResponse -> ResponseSerializers.offers as SerializationStrategy<IResponse>
                else -> null
            }
        }
        polymorphicDefault(IResponse::class) {
            AdResponseSerializer
        }
        polymorphicDefaultSerializer(IAdProduct::class) { instance ->
            @Suppress("UNCHECKED_CAST")
            when (instance) {
                is AdProductBolt -> ProductSerializers.bolt as SerializationStrategy<IAdProduct>
                else -> null
            }
        }
        polymorphicDefault(IAdProduct::class) {
            AdProductSerializer
        }
    }
}
