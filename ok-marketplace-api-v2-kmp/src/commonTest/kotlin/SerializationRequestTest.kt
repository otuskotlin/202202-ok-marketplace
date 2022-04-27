package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationRequestTest {
    val createRequest = AdCreateRequest(
        ad = AdCreateObject(
            title = "Title",
            description = "Description",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV2RequestSerialize(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"requestType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = apiV2RequestSerialize(createRequest)
        val decoded = apiV2RequestDeserialize<AdCreateRequest>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIRequestTest() {
        val jsonString = apiV2RequestSerialize(createRequest)
        val decoded = apiV2RequestDeserialize<IRequest>(jsonString) as AdCreateRequest
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }
}
