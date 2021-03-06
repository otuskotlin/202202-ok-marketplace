package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand

suspend fun ApplicationCall.createAd(service: AdService) =
    controllerHelperV2<AdReadRequest, AdReadResponse>(MkplCommand.CREATE) {
    service.createAd(this)
}

suspend fun ApplicationCall.readAd(service: AdService) =
    controllerHelperV2<AdReadRequest, AdReadResponse>(MkplCommand.READ) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: AdService) =
    controllerHelperV2<AdUpdateRequest, AdUpdateResponse>(MkplCommand.UPDATE) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: AdService) =
    controllerHelperV2<AdDeleteRequest, AdDeleteResponse>(MkplCommand.DELETE) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: AdService) =
    controllerHelperV2<AdSearchRequest, AdSearchResponse>(MkplCommand.SEARCH) {
        adService.searchAd(this)
    }
