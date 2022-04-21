package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse

/**
 * Сериализатор для десериализации Json-строки по значению дескриминатора
 */
internal object AdResponseSerializer : JsonContentPolymorphicSerializer<IResponse>(IResponse::class) {
    private const val discriminator = "responseType"
    override fun selectDeserializer(element: JsonElement): KSerializer<out IResponse> =
        when (val discriminatorValue = element.jsonObject[discriminator]?.jsonPrimitive?.content) {
            "create" -> ResponseSerializers.create
            "read"   -> ResponseSerializers.read
            "update" -> ResponseSerializers.update
            "delete" -> ResponseSerializers.delete
            "search" -> ResponseSerializers.search
            "offers" -> ResponseSerializers.offers
            else -> throw SerializationException(
                "Unknown value '${discriminatorValue}' in discriminator '$discriminator' " +
                        "property of IResponse implementation"
            )
        }
}
