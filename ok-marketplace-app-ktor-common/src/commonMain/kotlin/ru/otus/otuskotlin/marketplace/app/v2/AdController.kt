package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.common.models.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand

suspend fun ApplicationCall.createAd(service: AdService, principal: MkplPrincipalModel) =
    controllerHelperV2<AdReadRequest, AdReadResponse>(MkplCommand.CREATE, principal) {
    service.createAd(this)
}

suspend fun ApplicationCall.readAd(service: AdService, principal: MkplPrincipalModel) =
    controllerHelperV2<AdReadRequest, AdReadResponse>(MkplCommand.READ, principal) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: AdService, principal: MkplPrincipalModel) =
    controllerHelperV2<AdUpdateRequest, AdUpdateResponse>(MkplCommand.UPDATE, principal) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: AdService, principal: MkplPrincipalModel) =
    controllerHelperV2<AdDeleteRequest, AdDeleteResponse>(MkplCommand.DELETE, principal) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: AdService, principal: MkplPrincipalModel) =
    controllerHelperV2<AdSearchRequest, AdSearchResponse>(MkplCommand.SEARCH, principal) {
        adService.searchAd(this)
    }
