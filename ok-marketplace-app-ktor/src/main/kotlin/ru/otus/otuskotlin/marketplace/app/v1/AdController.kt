package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.app.mappers.toModel
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState

suspend fun ApplicationCall.createAd(adService: AdService) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
        principal = principal<JWTPrincipal>().toModel(),
    )
    try {
        val request = receive<AdCreateRequest>()
        ctx.fromTransport(request)
        adService.createAd(ctx)
        val response = ctx.toTransportAd()
        respond(response)
    } catch (e: Throwable) {
        ctx.command = MkplCommand.CREATE
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        adService.createAd(ctx)
        val response = ctx.toTransportAd()
        respond(response)
    }
}

suspend fun ApplicationCall.readAd(service: AdService) =
    controllerHelperV1<AdReadRequest, AdReadResponse>(MkplCommand.READ) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: AdService) =
    controllerHelperV1<AdUpdateRequest, AdUpdateResponse>(MkplCommand.UPDATE) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: AdService) =
    controllerHelperV1<AdDeleteRequest, AdDeleteResponse>(MkplCommand.DELETE) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: AdService) =
    controllerHelperV1<AdSearchRequest, AdSearchResponse>(MkplCommand.SEARCH) {
        adService.searchAd(this)
    }
