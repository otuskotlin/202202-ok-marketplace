package ru.otus.otuskotlin.marketplace.springapp.configs

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.springapp.api.v1.controller.WsHandlerV1
import ru.otus.otuskotlin.marketplace.springapp.api.v2.controller.WsHandlerV2

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val adService: AdService,
    private val offerService: OfferService,
    private val objectMapper: ObjectMapper,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WsHandlerV1(adService, offerService, objectMapper), "/ws/v1").setAllowedOrigins("*")
        registry.addHandler(WsHandlerV2(), "/ws/v2").setAllowedOrigins("*")
    }
}
