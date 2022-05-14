package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.api.v1.service.AdService
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*

@RestController
@RequestMapping("v1/ad")
class AdController(
    private val adService: AdService
) {

    @PostMapping("create")
    fun createAd(@RequestBody createAdRequest: AdCreateRequest): AdCreateResponse {
        val context = MkplContext().apply { fromTransport(createAdRequest) }

        return adService.createAd(context).toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody readAdRequest: AdReadRequest) =
        MkplContext().apply {
            fromTransport(readAdRequest)
        }.let {
            adService.readAd(it)
        }.toTransportRead()

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody updateAdRequest: AdUpdateRequest): AdUpdateResponse {
        return MkplContext().apply {
            fromTransport(updateAdRequest)
        }.let {
            adService.updateAd(it)
        }.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody deleteAdRequest: AdDeleteRequest): AdDeleteResponse {
        val context = MkplContext().apply { fromTransport(deleteAdRequest) }

        val result = adService.deleteAd(context)

        return result.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody searchAdRequest: AdSearchRequest) =
        MkplContext().apply { fromTransport(searchAdRequest) }.let {
            adService.searchAd(it)
        }.toTransportSearch()
}