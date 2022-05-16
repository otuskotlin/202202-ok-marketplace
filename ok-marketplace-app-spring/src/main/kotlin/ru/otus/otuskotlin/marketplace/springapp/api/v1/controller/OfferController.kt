package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportOffers
import ru.otus.otuskotlin.marketplace.springapp.api.v1.buildError

@RestController
@RequestMapping("v1/ad")
class OfferController(
    private val offerService: OfferService
) {

    @PostMapping("offers")
    fun searchOffers(@RequestBody offersAdRequest: AdOffersRequest) =
        MkplContext().apply { fromTransport(offersAdRequest) }.let {
            offerService.searchOffers(it, ::buildError)
        }.toTransportOffers()
}
