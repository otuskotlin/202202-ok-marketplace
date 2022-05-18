package ru.otus.otuskotlin.marketplace.api

import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.api.v1.v1Ad
import ru.otus.otuskotlin.marketplace.api.v1.v1Offer
import ru.otus.otuskotlin.marketplace.api.v2.v2Ad
import ru.otus.otuskotlin.marketplace.api.v2.v2Offer
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService

internal fun Routing.v1(adService: AdService, offerService: OfferService) {
    route("v1") {
        v1Ad(adService)
        v1Offer(offerService)
    }
}

internal fun Routing.v2(adService: AdService, offerService: OfferService) {
    route("v2") {
        v2Ad(adService)
        v2Offer(offerService)
    }
}
