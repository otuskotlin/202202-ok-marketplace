package ru.otus.otuskotlin.marketplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportOffers
import ru.otus.otuskotlin.marketplace.springapp.api.v2.buildError

@RestController
@RequestMapping("v2/ad")
class OfferControllerV2(
    private val offerServiceV2: OfferService
) {

    @PostMapping("offers")
    fun searchOffers(@RequestBody offersAdRequest: AdOffersRequest) =
        MkplContext().apply { fromTransport(offersAdRequest) }.let {
            offerServiceV2.searchOffers(it, ::buildError)
        }.toTransportOffers()
}
