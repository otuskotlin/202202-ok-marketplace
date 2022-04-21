package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*
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
                visibility = AdResponseObject.Visibility.PUBLIC
            )
        )
        val jsonString = apiV1ResponseSerialize(createResponse)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"responseType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = "{\"responseType\":\"create\",\"responseType\":null,\"requestId\":null,\"result\":null,\"errors\":null,\"ad\":{\"ad\":null,\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"permissions\":null},\"debug\":null}"
        val decoded = apiV1ResponseDeserialize<AdCreateResponse>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdResponseObject.Visibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIResponseTest() {
        val jsonString = "{\"responseType\":\"create\",\"responseType\":null,\"requestId\":null,\"result\":null,\"errors\":null,\"ad\":{\"ad\":null,\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"permissions\":null},\"debug\":null}"
        val decoded = apiV1ResponseDeserialize<IResponse>(jsonString) as AdCreateResponse
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdResponseObject.Visibility.PUBLIC, decoded.ad?.visibility)
    }
}
