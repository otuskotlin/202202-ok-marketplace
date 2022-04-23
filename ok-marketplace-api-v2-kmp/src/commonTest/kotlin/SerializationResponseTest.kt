package ru.otus.otuskotlin.marketplace.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationResponseTest {

    @Test
    fun serializeTest() {
        val createResponse = AdCreateResponse(
            ad = AdResponseObject(
                title = "Title",
                description = "Description",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC
            )
        )
        val jsonString = apiV2ResponseSerialize(createResponse)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"responseType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = "{\"responseType\":\"create\",\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"props\":null},\"debug\":null}"
        val decoded = apiV2ResponseDeserialize<AdCreateResponse>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIResponseTest() {
        val jsonString = "{\"responseType\":\"create\",\"requestId\":null,\"result\":null,\"errors\":null,\"ad\":{\"ad\":null,\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"props\":null,\"permissions\":null},\"debug\":null}"
        val decoded = apiV2ResponseDeserialize<IResponse>(jsonString) as AdCreateResponse
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }
}
