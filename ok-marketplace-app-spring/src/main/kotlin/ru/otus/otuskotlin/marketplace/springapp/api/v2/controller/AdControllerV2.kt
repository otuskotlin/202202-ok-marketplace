package ru.otus.otuskotlin.marketplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*
import ru.otus.otuskotlin.marketplace.springapp.api.v2.buildError

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(
    private val adService: AdService
) {

    @PostMapping("create")
    fun createAd(@RequestBody createAdRequest: AdCreateRequest): AdCreateResponse {

//        val context = MkplContext().apply { fromTransport(apiV2RequestDeserialize<IRequest>(createAdRequest)) }
        val context = MkplContext().apply { fromTransport(createAdRequest) }

        return adService.createAd(context).toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody readAdRequest: AdReadRequest) =
        MkplContext().apply {
            fromTransport(readAdRequest)
        }.let {
            adService.readAd(it, ::buildError)
        }.toTransportRead()

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody updateAdRequest: AdUpdateRequest): AdUpdateResponse {
        return MkplContext().apply {
            fromTransport(updateAdRequest)
        }.let {
            adService.updateAd(it, ::buildError)
        }.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody deleteAdRequest: AdDeleteRequest): AdDeleteResponse {
        val context = MkplContext().apply { fromTransport(deleteAdRequest) }

        val result = adService.deleteAd(context, ::buildError)

        return result.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody searchAdRequest: AdSearchRequest) =
        MkplContext().apply { fromTransport(searchAdRequest) }.let {
            adService.searchAd(it, ::buildError)
        }.toTransportSearch()
}
