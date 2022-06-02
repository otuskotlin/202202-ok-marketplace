package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService

suspend fun ApplicationCall.createAd(service: AdService) = controllerHelperV2<AdReadRequest, AdReadResponse> {
    service.createAd(this)
}

suspend fun ApplicationCall.readAd(service: AdService) = controllerHelperV2<AdReadRequest, AdReadResponse> {
    service.readAd(this)
}

suspend fun ApplicationCall.updateAd(service: AdService) = controllerHelperV2<AdUpdateRequest, AdUpdateResponse> {
    service.updateAd(this)
}

suspend fun ApplicationCall.deleteAd(service: AdService) = controllerHelperV2<AdDeleteRequest, AdDeleteResponse> {
    service.deleteAd(this)
}

suspend fun ApplicationCall.searchAd(adService: AdService) = controllerHelperV2<AdSearchRequest, AdSearchResponse> {
    adService.searchAd(this)
}
