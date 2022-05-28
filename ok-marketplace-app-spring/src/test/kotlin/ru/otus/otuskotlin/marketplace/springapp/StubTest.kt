package ru.otus.otuskotlin.marketplace.springapp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.marketplace.api.v1.models.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class StubTest {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun create() {
        println("PORT: $port")
        webTestClient
            .post()
            .uri("http://localhost:$port/v1/ad/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(AdCreateRequest(
                requestType = "create",
                debug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)
            ))
            .exchange()
            .expectStatus().isOk()
            .expectBody(AdCreateResponse::class.java).consumeWith {
                val dat = it.responseBody
                assertThat(dat?.ad?.id).isEqualTo("666")

            }
    }
}
