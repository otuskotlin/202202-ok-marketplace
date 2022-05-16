package ru.otus.otuskotlin.marketplace.api.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService

fun Route.v1Ad(adService: AdService) {
    post("create") {
        call.createAd(adService)
    }
    post("read") {
        call.readAd(adService)
    }
    post("update") {
        call.updateAd(adService)
    }
    post("delete") {
        call.deleteAd(adService)
    }
    post("search") {
        call.searchAd(adService)
    }
}

fun Route.v1Offer(offerService: OfferService) {
    post("offers") {
        call.offersAd(offerService)
    }
}
