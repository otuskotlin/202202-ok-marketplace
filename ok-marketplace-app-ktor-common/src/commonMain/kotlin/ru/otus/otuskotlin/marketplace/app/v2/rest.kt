package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.services.AdService

// Я не смог добавить сюда зависимость от JWTPrincipal и поэтому передаю коллбэк
fun Route.v2Ad(adService: AdService, principalSupplier: ApplicationCall.() -> MkplPrincipalModel) {
    route("ad") {
        post("create") {
            call.createAd(adService, call.principalSupplier())
        }
        post("read") {
            call.readAd(adService, call.principalSupplier())
        }
        post("update") {
            call.updateAd(adService, call.principalSupplier())
        }
        post("delete") {
            call.deleteAd(adService, call.principalSupplier())
        }
        post("search") {
            call.searchAd(adService, call.principalSupplier())
        }
    }
}

fun Route.v2Offer(service: AdService, principalSupplier: ApplicationCall.() -> MkplPrincipalModel) {
    route("ad") {
        post("offers") {
            call.offersAd(service, call.principalSupplier())
        }
    }
}
