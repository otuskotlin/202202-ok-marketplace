package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationRequestProductTest {
    val createRequest = AdCreateRequest(
        ad = BaseAdUpdateable(
            title = "Title",
            description = "Description",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
            props = AdProductBolt(
                length = 25.0,
                diameter = 8.0,
                headStyle = AdProductBolt.HeadStyle.HEXAGON_FLANGE,
                thread = AdProductBoltThread(
                    pitch = 1.0,
                    pitchConf = AdProductBoltThread.PitchConf.COARSE,
                )
            )
        )
    )

    @Test
    fun serializeTest() {
        val jsonString = apiV2RequestSerialize(createRequest)
//        val jsonString = serializationMapper.encodeToString(createRequest)
//        val jsonString = serializationMapper.encodeToString(AdRequestSerializer, createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
        assertContains(jsonString, """"requestType":"create"""")
        assertContains(jsonString, """"pitchConf":"coarse"""")

        val decoded = serializationMapper.decodeFromString<AdCreateRequest>(jsonString)
        println(decoded)
//        val decoded = apiV2RequestDeserialize<AdCreateRequest>(jsonString)
        assertEquals(AdProductBoltThread.PitchConf.COARSE, (decoded.ad?.props as? AdProductBolt)?.thread?.pitchConf)
        assertEquals("bolt", (decoded.ad?.props as? AdProductBolt)?.productType)
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
//
//    @Test
//    fun deserializeIRequestTest() {
//        val jsonString = "{\"requestType\":\"create\",\"requestId\":null,\"ad\":{\"title\":\"Title\",\"description\":\"Description\",\"ownerId\":null,\"adType\":\"demand\",\"visibility\":\"public\",\"props\":null},\"debug\":null}"
//        val decoded = apiV2RequestDeserialize<IRequest>(jsonString) as AdCreateRequest
//        println(decoded)
//        assertEquals("Title", decoded.ad?.title)
//        assertEquals("Description", decoded.ad?.description)
//        assertEquals(DealSide.DEMAND, decoded.ad?.adType)
//        assertEquals(AdVisibility.PUBLIC, decoded.ad?.visibility)
//    }
}
