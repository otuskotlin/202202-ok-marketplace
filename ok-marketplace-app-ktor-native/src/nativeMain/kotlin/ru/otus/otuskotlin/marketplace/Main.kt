package ru.otus.otuskotlin.marketplace

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.app.v2.CustomJsonConverter
import ru.otus.otuskotlin.marketplace.app.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.v2.v2Offer
import ru.otus.otuskotlin.marketplace.backend.services.AdService

fun main() {
    embeddedServer(CIO, port = 8080) {
        install(Routing)
        install(ContentNegotiation) {
            register(ContentType.Application.Json, CustomJsonConverter())
        }

        val service = AdService()

        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
            route("v2") {
                v2Ad(service)
                v2Offer(service)
            }
        }
    }.start(wait = true)
}
