package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService

fun Route.v2Ad(adService: AdService) {
    route("ad") {
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
}

fun Route.v2Offer(service: AdService) {
    route("ad") {
        post("offers") {
            call.offersAd(service)
        }
    }
}
