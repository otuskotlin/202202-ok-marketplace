package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.backend.services.AdService

suspend fun ApplicationCall.offersAd(service: AdService) = controllerHelperV1<AdOffersRequest, AdOffersResponse> {
    service.searchOffers(this)
}
