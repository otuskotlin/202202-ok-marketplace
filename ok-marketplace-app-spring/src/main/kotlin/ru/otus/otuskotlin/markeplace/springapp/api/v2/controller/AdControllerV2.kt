package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.api.v2.service.AdServiceV2
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(
    private val adServiceV2: AdServiceV2
) {

    @PostMapping("create")
    fun createAd(@RequestBody createAdRequest: AdCreateRequest): AdCreateResponse {

//        val context = MkplContext().apply { fromTransport(apiV2RequestDeserialize<IRequest>(createAdRequest)) }
        val context = MkplContext().apply { fromTransport(createAdRequest) }

        return adServiceV2.createAd(context).toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody readAdRequest: AdReadRequest) =
        MkplContext().apply {
            fromTransport(readAdRequest)
        }.let {
            adServiceV2.readAd(it)
        }.toTransportRead()

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody updateAdRequest: AdUpdateRequest): AdUpdateResponse {
        return MkplContext().apply {
            fromTransport(updateAdRequest)
        }.let {
            adServiceV2.updateAd(it)
        }.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody deleteAdRequest: AdDeleteRequest): AdDeleteResponse {
        val context = MkplContext().apply { fromTransport(deleteAdRequest) }

        val result = adServiceV2.deleteAd(context)

        return result.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody searchAdRequest: AdSearchRequest) =
        MkplContext().apply { fromTransport(searchAdRequest) }.let {
            adServiceV2.searchAd(it)
        }.toTransportSearch()
}