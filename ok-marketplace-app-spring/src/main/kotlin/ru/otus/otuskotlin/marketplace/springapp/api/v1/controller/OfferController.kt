package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.logging.mpLogger

@RestController
@RequestMapping("v1/ad")
class OfferController(
    private val service: AdService
) {
    private val logger = mpLogger(this::class.java)
    @PostMapping("offers")
    fun searchOffers(@RequestBody request: AdOffersRequest): AdOffersResponse =
        controllerHelperV1(request, logger, "ad-offers", MkplCommand.OFFERS) {
            service.searchOffers(this)
        }
}
