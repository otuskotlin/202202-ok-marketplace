package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Делегат сериализаторов для заполнения дескриминатора корректным значением
 */
internal class ResponseSerializer<T: IResponse>(private val serializer: KSerializer<T>): KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) {
        val request = when(value) {
            is AdCreateResponse -> value.copy(responseType = "create")
            is AdReadResponse   -> value.copy(responseType = "read")
            is AdUpdateResponse -> value.copy(responseType = "update")
            is AdDeleteResponse -> value.copy(responseType = "delete")
            is AdSearchResponse -> value.copy(responseType = "search")
            is AdOffersResponse -> value.copy(responseType = "offers")
            else -> throw SerializationException(
                "Unknown class to serialize as IRequest instance in RequestSerializer"
            )
        }
        @Suppress("UNCHECKED_CAST")
        return serializer.serialize(encoder, request as T)
    }
}
