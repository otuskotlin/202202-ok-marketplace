package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.v2.offersAd
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.logging.mpLogger

private val logger = mpLogger(ApplicationCall::offersAd::class.java)

suspend fun ApplicationCall.offersAd(service: AdService) =
    controllerHelperV1<AdOffersRequest, AdOffersResponse>(
        logger = logger,
        logId = "ad-offers",
        command = MkplCommand.OFFERS
    ) {
        service.searchOffers(this)
    }
