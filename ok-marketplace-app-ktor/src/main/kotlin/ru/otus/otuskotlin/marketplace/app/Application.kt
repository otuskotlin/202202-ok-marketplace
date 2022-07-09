package ru.otus.otuskotlin.marketplace.app

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.marketplace.app.config.jsonConfig
import ru.otus.otuskotlin.marketplace.app.v1.mpWsHandlerV1
import ru.otus.otuskotlin.marketplace.app.v1.v1Ad
import ru.otus.otuskotlin.marketplace.app.v1.v1Offer
import ru.otus.otuskotlin.marketplace.app.v2.*
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(KtorExperimentalLocationsAPI::class)
@Suppress("unused") // Referenced in application.conf
fun Application.module(
    adRepo: IAdRepository? = null,
) {
    // Generally not needed as it is replaced by a `routing`
    install(Routing)

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(WebSockets)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }

    install(ContentNegotiation) {
        jackson {
            jsonConfig()
        }
        register(ContentType.Application.Json, CustomJsonConverter())
    }


    install(CallLogging) {
        level = Level.INFO
    }

    install(Locations)

    val repo by lazy { adRepo ?: AdRepoInMemory() }
    val service = AdService(adRepositoty = repo)
    val sessions = mutableSetOf<KtorUserSession>()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        route("v1") {
            v1Ad(service)
            v1Offer(service)
        }
        route("v2") {
            v2Ad(service)
            v2Offer(service)
        }

        static("static") {
            resources("static")
        }
        webSocket("/ws/v1"){
            mpWsHandlerV1(service, sessions)
        }
        webSocket("/ws/v2") {
            mpWsHandlerV2(service, sessions)
        }
    }
}
