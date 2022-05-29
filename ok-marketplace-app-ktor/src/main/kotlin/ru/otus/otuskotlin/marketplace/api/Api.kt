package ru.otus.otuskotlin.marketplace.api

import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.api.v1.v1Ad
import ru.otus.otuskotlin.marketplace.api.v1.v1Offer
import ru.otus.otuskotlin.marketplace.api.v2.v2Ad
import ru.otus.otuskotlin.marketplace.api.v2.v2Offer
import ru.otus.otuskotlin.marketplace.backend.services.AdService

internal fun Routing.v1(service: AdService) {
    route("v1") {
        v1Ad(service)
        v1Offer(service)
    }
}

internal fun Routing.v2(service: AdService) {
    route("v2") {
        v2Ad(service)
        v2Offer(service)
    }
}
