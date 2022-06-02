package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend fun ApplicationCall.createAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdCreateRequest>()
    ctx.fromTransport(request)
    adService.createAd(ctx)
    respond(ctx.toTransportAd())
}

suspend fun ApplicationCall.readAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdReadRequest>()
    ctx.fromTransport(request)
    adService.readAd(ctx)
    respond(ctx.toTransportAd())
}

suspend fun ApplicationCall.updateAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdUpdateRequest>()
    ctx.fromTransport(request)
    adService.updateAd(ctx)
    respond(ctx.toTransportAd())
}

suspend fun ApplicationCall.deleteAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdDeleteRequest>()
    ctx.fromTransport(request)
    adService.deleteAd(ctx)
    respond(ctx.toTransportAd())
}

suspend fun ApplicationCall.searchAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdSearchRequest>()
    ctx.fromTransport(request)
    adService.searchAd(ctx)
    respond(ctx.toTransportAd())
}
