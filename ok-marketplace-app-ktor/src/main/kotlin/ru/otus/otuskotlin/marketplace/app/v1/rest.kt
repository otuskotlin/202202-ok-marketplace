package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService

fun Route.v1Ad(service: AdService) {
    route("ad") {
        post("create") {
            call.createAd(service)
        }
        post("read") {
            call.readAd(service)
        }
        post("update") {
            call.updateAd(service)
        }
        post("delete") {
            call.deleteAd(service)
        }
        post("search") {
            call.searchAd(service)
        }
    }
}

fun Route.v1Offer(service: AdService) {
    route("ad") {
        post("offers") {
            call.offersAd(service)
        }
    }
}
