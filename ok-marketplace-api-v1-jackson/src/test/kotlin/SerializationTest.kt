package ru.otus.otuskotlin.marketplace.api.v1

import org.junit.Test
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.BaseAdUpdateable
import ru.otus.otuskotlin.marketplace.api.v1.models.DealSide
import kotlin.test.assertContains

class SerializationTest {

    @Test
    fun serTest() {
        val createRequest = AdCreateRequest(
            ad = BaseAdUpdateable(
                title = "Title",
                description = "Description",
                adType = DealSide.DEMAND,
                visibility = BaseAdUpdateable.Visibility.PUBLIC
            )
        )
        val jsonString = jacksonMapper.writeValueAsString(createRequest)
        println(jsonString)
        assertContains(jsonString, """"title":"Title"""")
    }
}
