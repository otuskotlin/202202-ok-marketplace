package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.logging.mpLogger

@RestController
@RequestMapping("v1/ad")
class AdController(
    private val adService: AdService,
) {
    private val logger = mpLogger(this::class.java)

    @PostMapping("create")
    fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse =
        controllerHelperV1(request, logger, "ad-create", MkplCommand.CREATE) {
            adService.createAd(this)
        }

    @PostMapping("read")
    fun readAd(@RequestBody request: AdReadRequest): AdReadResponse =
        controllerHelperV1(request, logger, "ad-read", MkplCommand.READ) {
            adService.readAd(this)
        }

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        controllerHelperV1(request, logger, "ad-update", MkplCommand.UPDATE) {
            adService.updateAd(this)
        }

    @PostMapping("delete")
    fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        controllerHelperV1(request, logger, "ad-delete", MkplCommand.DELETE) {
            adService.deleteAd(this)
        }

    @PostMapping("search")
    fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse =
        controllerHelperV1(request, logger, "ad-search", MkplCommand.SEARCH) {
            adService.searchAd(this)
        }
}
