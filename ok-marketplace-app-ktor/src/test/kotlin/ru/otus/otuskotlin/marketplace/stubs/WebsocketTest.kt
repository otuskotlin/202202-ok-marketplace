package ru.otus.otuskotlin.marketplace.stubs

import io.ktor.client.plugins.websocket.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import ru.otus.otuskotlin.marketplace.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WebsocketTest {

    @Test
    fun create() {
        testApplication {
            val client = createClient {
                install(WebSockets)
            }

            client.webSocket("/ws/v1") {
                kotlin.run {
                    val incoming = incoming.receive()
                    val response = apiV1ResponseDeserialize<IResponse>((incoming as Frame.Text).readText())
                    assertIs<AdReadResponse>(response)
                }
                val request = AdCreateRequest(
                    requestId = "12345",
                    ad = AdCreateObject(
                        title = "Болт",
                        description = "КРУТЕЙШИЙ",
                        adType = DealSide.DEMAND,
                        visibility = AdVisibility.PUBLIC,
                    ),
                    debug = AdDebug(
                        mode = AdRequestDebugMode.STUB,
                        stub = AdRequestDebugStubs.SUCCESS
                    )
                )
                send(Frame.Text(apiV1RequestSerialize(request)))
                val incoming = incoming.receive()
                val response = apiV1ResponseDeserialize<AdCreateResponse>((incoming as Frame.Text).readText())
                assertEquals("666", response.ad?.id)
                assertEquals("Болт", response.ad?.title)
            }
        }
    }
}
