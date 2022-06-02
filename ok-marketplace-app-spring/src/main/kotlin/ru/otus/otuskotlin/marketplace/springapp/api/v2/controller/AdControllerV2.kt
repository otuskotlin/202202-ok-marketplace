package ru.otus.otuskotlin.marketplace.springapp.api.v2.controller

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(
    private val adService: AdService
) {

    @PostMapping("create")
    fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse {
        val ctx = MkplContext(
            timeStart = Clock.System.now(),
        )
        ctx.fromTransport(request)
        runBlocking { adService.createAd(ctx) }
        return ctx.toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody request: AdReadRequest): AdReadResponse {
        val ctx = MkplContext(
            timeStart = Clock.System.now(),
        )
        ctx.fromTransport(request)
        runBlocking { adService.readAd(ctx) }
        return ctx.toTransportRead()
    }

    @RequestMapping("update", method = [RequestMethod.POST])
    fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse {
        val ctx = MkplContext(
            timeStart = Clock.System.now(),
        )
        ctx.fromTransport(request)
        runBlocking { adService.updateAd(ctx) }
        return ctx.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse {
        val ctx = MkplContext(
            timeStart = Clock.System.now(),
        )
        ctx.fromTransport(request)
        runBlocking { adService.deleteAd(ctx) }
        return ctx.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse {
        val ctx = MkplContext(
            timeStart = Clock.System.now(),
        )
        ctx.fromTransport(request)
        runBlocking { adService.searchAd(ctx) }
        return ctx.toTransportSearch()

    }
}
