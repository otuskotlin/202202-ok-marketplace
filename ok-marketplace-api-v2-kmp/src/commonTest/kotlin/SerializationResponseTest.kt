package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {
    val createResponse = AdCreateResponse(
        ad = AdResponseObject(
            title = "Title",
            description = "Description",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV2ResponseSerialize(createResponse)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"responseType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = apiV2ResponseSerialize(createResponse)
        val decoded = apiV2ResponseDeserialize<AdCreateResponse>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIResponseTest() {
        val jsonString = apiV2ResponseSerialize(createResponse)
        val decoded = apiV2ResponseDeserialize<IResponse>(jsonString) as AdCreateResponse
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }
}
