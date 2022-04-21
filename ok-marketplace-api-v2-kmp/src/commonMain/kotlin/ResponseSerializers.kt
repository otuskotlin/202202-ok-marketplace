package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*

internal object ResponseSerializers {
    val create = ResponseSerializer(AdCreateResponse.serializer())
    val read   = ResponseSerializer(AdReadResponse.serializer())
    val update = ResponseSerializer(AdUpdateResponse.serializer())
    val delete = ResponseSerializer(AdDeleteResponse.serializer())
    val search = ResponseSerializer(AdSearchResponse.serializer())
    val offers = ResponseSerializer(AdOffersResponse.serializer())
}
