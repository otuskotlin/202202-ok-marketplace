package ru.otus.otuskotlin.marketplace

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.v2.*

fun main() {
    embeddedServer(CIO, port = 8080) {
        install(Routing)

        val a: AdCreateRequest? = null
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        val adService = AdService()
        val offerService = OfferService()

        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
            route("v1") {
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

                    post("offers") {
                        call.offersAd(offerService)
                    }
                }
            }
        }
    }.start(wait = true)
}
