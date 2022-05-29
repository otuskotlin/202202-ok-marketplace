package ru.otus.otuskotlin.marketplace.api.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import kotlinx.datetime.Clock

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

suspend fun ApplicationCall.deleteAd(service: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdDeleteRequest>()
    ctx.fromTransport(request)
    service.deleteAd(ctx)
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
