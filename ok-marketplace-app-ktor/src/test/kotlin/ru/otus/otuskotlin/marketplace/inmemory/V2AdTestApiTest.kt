package ru.otus.otuskotlin.marketplace.inmemory

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.module
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.common.models.*
import kotlin.test.assertEquals

class V2AdTestApiTest {

    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val initAd = MkplAd(
        id = MkplAdId(uuidOld),
        title = "abc",
        description = "abc",
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        lock = MkplAdLock(uuidOld),
    )


    @Test
    fun create() = testApplication {
        application {
            val repo by lazy { AdRepoInMemory(randomUuid = { uuidNew }) }
            val settings by lazy {
                MkplSettings(repoProd = repo)
            }
            module(settings)
        }

        val response = client.post("/v2/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "12345",
                ad = AdCreateObject(
                    title = "Болт",
                    description = "КРУТЕЙШИЙ",
                    adType = DealSide.DEMAND,
                    visibility = AdVisibility.PUBLIC,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(apiV2RequestSerialize(requestObj))
        }
        val responseObj = apiV2ResponseDeserialize<AdCreateResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.ad?.id)
        assertEquals(uuidNew, responseObj.ad?.lock)
    }

    @Test
    fun read() = testApplication {
        application {
            val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew }) }
            val settings by lazy {
                MkplSettings(repoProd = repo)
            }
            module(settings)
        }
        val response = client.post("/v2/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = AdIdObject(uuidOld),
            )
            contentType(ContentType.Application.Json)
            setBody(apiV2RequestSerialize(requestObj))
        }
        val responseObj = apiV2ResponseDeserialize<AdReadResponse>(response.bodyAsText())
        println(responseObj)
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
        application {
            val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew }) }
            val settings by lazy {
                MkplSettings(repoProd = repo)
            }
            module(settings)
        }
        val response = client.post("/v2/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = AdUpdateObject(
                    id = uuidOld,
                    title = "Болт",
                    description = "КРУТЕЙШИЙ",
                    adType = DealSide.DEMAND,
                    visibility = AdVisibility.PUBLIC,
                    lock = uuidOld,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(apiV2RequestSerialize(requestObj))
        }
        val responseObj = apiV2ResponseDeserialize<AdUpdateResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.ad?.id)
    }

    @Test
    fun delete() = testApplication {
        application {
            val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew }) }
            val settings by lazy {
                MkplSettings(repoProd = repo)
            }
            module(settings)
        }
        val response = client.post("/v2/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = uuidOld,
                    lock = uuidNew,
                ),
            )
            contentType(ContentType.Application.Json)
            setBody(apiV2RequestSerialize(requestObj))
        }
        val responseObj = apiV2ResponseDeserialize<AdDeleteResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.ad?.id)
        assertEquals(uuidOld, responseObj.ad?.lock)
    }

    @Test
    fun search() = testApplication {
        val response = client.post("/v2/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
            )
            contentType(ContentType.Application.Json)
            setBody(apiV2RequestSerialize(requestObj))
        }
        val responseObj = apiV2ResponseDeserialize<AdSearchResponse>(response.bodyAsText())
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = testApplication {
        val response = client.post("/v2/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = "12345",
                ad = AdIdObject(id = uuidOld),
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2RequestSerialize(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2ResponseDeserialize<AdOffersResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
        assertEquals("s-666-01", responseObj.offers?.first()?.id)
    }

}
