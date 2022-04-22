package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Список сериализаторов для всех объектов
 */
internal object ProductSerializers {
    val bolt = ProductSerializer(AdProductBolt.serializer())
}
