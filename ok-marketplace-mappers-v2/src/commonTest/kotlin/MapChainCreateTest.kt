package ru.otus.otuskotlin.marketplace.mappers.v2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MapChainCreateTest {
    @Test
    fun create() = runTest {
        val request = AdCreateRequest(
            requestId = "12343",
            ad = AdCreateObject(
                title = "tit1",
                description = "des1",
                adType = DealSide.DEMAND,
                visibility = AdVisibility.PUBLIC,
                product = AdProductBolt(
                    length = 100.0,
                    diameter = 5.0,
                    headStyle = AdProductBolt.HeadStyle.HEXAGON_FLANGE,
                    thread = AdProductBoltThread(
                        pitch = 0.3,
                        pitchConf = AdProductBoltThread.PitchConf.FINE
                    )
                )
            )
        )
        val ctx = MkplContext()
        ctx.chainFromTransport(request)
        assertEquals("tit1", ctx.adRequest.title)
        assertEquals("des1", ctx.adRequest.description)
    }
}
