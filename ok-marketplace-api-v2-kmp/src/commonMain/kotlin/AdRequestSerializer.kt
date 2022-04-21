package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest

/**
 * Сериализатор для десериализации Json-строки по значению дескриминатора
 */
internal object AdRequestSerializer : JsonContentPolymorphicSerializer<IRequest>(IRequest::class) {
    private const val discriminator = "requestType"
    override fun selectDeserializer(element: JsonElement): KSerializer<out IRequest> =
        when (val discriminatorValue = element.jsonObject[discriminator]?.jsonPrimitive?.content) {
            "create" -> RequestSerializers.create
            "read"   -> RequestSerializers.read
            "update" -> RequestSerializers.update
            "delete" -> RequestSerializers.delete
            "search" -> RequestSerializers.search
            "offers" -> RequestSerializers.offers
            else -> throw SerializationException(
                "Unknown value '${discriminatorValue}' in discriminator '$discriminator' " +
                        "property of IRequest implementation"
            )
        }
}
