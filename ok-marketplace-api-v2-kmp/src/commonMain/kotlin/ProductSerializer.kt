package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Делегат сериализаторов для заполнения дескриминатора корректным значением
 */
internal class ProductSerializer<T: IAdProduct>(private val serializer: KSerializer<T>): KSerializer<T> by serializer {
    override fun serialize(encoder: Encoder, value: T) {
        val request = when(value) {
            is AdProductBolt -> value.copy(productType = "bolt")
            else -> throw SerializationException(
                "Unknown class to serialize as IRequest instance in RequestSerializer"
            )
        }
        @Suppress("UNCHECKED_CAST")
        return serializer.serialize(encoder, request as T)
    }
}
