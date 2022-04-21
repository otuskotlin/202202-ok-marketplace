package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.PolymorphicSerializer
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationRequestTest {

    @Test
    fun serializeTest() {
        val createRequest = AdCreateRequest(
            ad = BaseAdUpdateable(
                title = "Title",
                description = "Description",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC
            )
        )
        val jsonString = apiV2Serialize(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"requestType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = "{\"requestType\":\"create\",\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"props\":null},\"debug\":null}"
        val decoded = apiV2Deserialize<AdCreateRequest>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIRequestTest() {
        val jsonString = "{\"requestType\":\"create\",\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"props\":null},\"debug\":null}"
        val decoded = apiV2Deserialize<IRequest>(jsonString) as AdCreateRequest
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
    }
}
