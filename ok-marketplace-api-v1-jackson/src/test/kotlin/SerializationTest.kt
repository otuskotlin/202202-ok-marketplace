package ru.otus.otuskotlin.marketplace.api.v1

import org.junit.Test
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.assertContains

class SerializationTest {

    @Test
    fun serTest() {
        val createRequest = AdCreateRequest(
            ad = AdCreateObject(
                title = "Title",
                description = "Description",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
    }
}
