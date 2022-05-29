package ru.otus.otuskotlin.marketplace.api.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

suspend fun ApplicationCall.offersAd(service: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    val request = receive<AdOffersRequest>()
    ctx.fromTransport(request)
    service.searchOffers(ctx)
    respond(ctx.toTransportAd())
}
