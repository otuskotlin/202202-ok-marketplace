package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder
import ru.otus.otuskotlin.marketplace.api.v2.models.*

internal class RequestSerializer<T: IRequest>(private val serializer: KSerializer<T>): KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) {
        val request = when(value) {
            is AdCreateRequest -> value.copy(requestType = "create")
            is AdReadRequest -> value.copy(requestType = "read")
            is AdUpdateRequest -> value.copy(requestType = "update")
            is AdDeleteRequest -> value.copy(requestType = "delete")
            is AdSearchRequest -> value.copy(requestType = "search")
            is AdOffersRequest -> value.copy(requestType = "offers")
            else -> throw SerializationException(
                "Unknown class to serialize as IRequest instance in RequestSerializer"
            )
        }
        return serializer.serialize(encoder, request as T)
    }
}
