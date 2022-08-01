package ru.otus.otuskotlin.marketplace.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.marketplace.app.config.KtorAuthConfig
import ru.otus.otuskotlin.marketplace.app.config.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.marketplace.app.config.jsonConfig
import ru.otus.otuskotlin.marketplace.app.mappers.toModel
import ru.otus.otuskotlin.marketplace.app.v1.mpWsHandlerV1
import ru.otus.otuskotlin.marketplace.app.v1.v1Ad
import ru.otus.otuskotlin.marketplace.app.v1.v1Offer
import ru.otus.otuskotlin.marketplace.app.v2.KtorUserSession
import ru.otus.otuskotlin.marketplace.app.v2.mpWsHandlerV2
import ru.otus.otuskotlin.marketplace.app.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.v2.v2Offer
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplSettings

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module(
    settings: MkplSettings? = null,
    authConfig: KtorAuthConfig = KtorAuthConfig(environment)
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
    }

    val corSettings by lazy {
        settings ?: MkplSettings(
            repoTest = AdRepoInMemory(),
//            repoProd = AdRepoGremlin(
//
//            ),
        )
    }
    val service by lazy { AdService(corSettings) }
    val sessions = mutableSetOf<KtorUserSession>()

    //    https://github.com/imalik8088/ktor-starter/blob/master/src/Application.kt
    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(authConfig.secret))
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            )
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        authenticate("auth-jwt") {
            route("v1") {
                v1Ad(service)
                v1Offer(service)
            }
            route("v2") {
                v2Ad(service) { principal<JWTPrincipal>().toModel() }
                v2Offer(service)  { principal<JWTPrincipal>().toModel() }
            }
        }

        static("static") {
            resources("static")
        }
        webSocket("/ws/v1") {
            mpWsHandlerV1(service, sessions)
        }
        webSocket("/ws/v2") {
            mpWsHandlerV2(service, sessions)
        }
    }
}
