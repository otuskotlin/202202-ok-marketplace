package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.logging.MpLogWrapper

suspend fun ApplicationCall.createAd(service: AdService, logger: MpLogWrapper) =
    controllerHelperV1<AdCreateRequest, AdCreateResponse>(
        logger = logger,
        logId = "ad-create",
        command = MkplCommand.CREATE
    ) {
        service.createAd(this)
    }

suspend fun ApplicationCall.readAd(service: AdService, logger: MpLogWrapper) =
    controllerHelperV1<AdReadRequest, AdReadResponse>(
        logger = logger,
        logId = "ad-read",
        MkplCommand.READ
    ) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: AdService, logger: MpLogWrapper) =
    controllerHelperV1<AdUpdateRequest, AdUpdateResponse>(
        logger = logger,
        logId = "ad-update",
        MkplCommand.UPDATE
    ) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: AdService, logger: MpLogWrapper) =
    controllerHelperV1<AdDeleteRequest, AdDeleteResponse>(
        logger = logger,
        logId = "ad-delete",
        MkplCommand.DELETE
    ) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: AdService, logger: MpLogWrapper) =
    controllerHelperV1<AdSearchRequest, AdSearchResponse>(
        logger = logger,
        logId = "ad-search",
        MkplCommand.SEARCH
    ) {
        adService.searchAd(this)
    }
