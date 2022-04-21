package ru.otus.otuskotlin.marketplace.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.BaseAdUpdateable
import ru.otus.otuskotlin.marketplace.api.v1.models.DealSide
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
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
                visibility = BaseAdUpdateable.Visibility.PUBLIC
            )
        )
        val jsonString = apiV1RequestSerialize(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"requestType":"create"""")

    }
    @Test
    fun deserializeTest() {
        val jsonString = "{\"requestType\":\"create\",\"requestType\":null,\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\"},\"debug\":null}"
        val decoded = apiV1RequestDeserialize<AdCreateRequest>(jsonString)
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(BaseAdUpdateable.Visibility.PUBLIC, decoded.ad?.visibility)
    }

    @Test
    fun deserializeIRequestTest() {
        val jsonString = "{\"requestType\":\"create\",\"requestType\":null,\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\"},\"debug\":null}"
        val decoded = apiV1RequestDeserialize<IRequest>(jsonString) as AdCreateRequest
        println(decoded)
        assertEquals("Title", decoded.ad?.title)
        assertEquals("Description", decoded.ad?.description)
        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
        assertEquals(BaseAdUpdateable.Visibility.PUBLIC, decoded.ad?.visibility)
    }
}
