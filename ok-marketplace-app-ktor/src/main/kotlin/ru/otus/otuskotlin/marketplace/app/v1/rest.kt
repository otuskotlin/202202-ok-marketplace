package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.logging.mpLogger

private val logger = mpLogger(Route::v1Ad::class.java)
fun Route.v1Ad(service: AdService) {
    route("ad") {
        post("create") {
            call.createAd(service, logger)
        }
        post("read") {
            call.readAd(service, logger)
        }
        post("update") {
            call.updateAd(service, logger)
        }
        post("delete") {
            call.deleteAd(service, logger)
        }
        post("search") {
            call.searchAd(service, logger)
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
