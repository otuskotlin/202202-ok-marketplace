package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Список сериализаторов для всех объектов
 */
internal object RequestSerializers {
    val create = RequestSerializer(AdCreateRequest.serializer())
    val read   = RequestSerializer(AdReadRequest.serializer())
    val update = RequestSerializer(AdUpdateRequest.serializer())
    val delete = RequestSerializer(AdDeleteRequest.serializer())
    val search = RequestSerializer(AdSearchRequest.serializer())
    val offers = RequestSerializer(AdOffersRequest.serializer())
}
