package ru.otus.otuskotlin.marketplace.springapp.configs

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.springapp.api.v2.controller.WsOfferHandlerV2
import ru.otus.otuskotlin.marketplace.springapp.api.v1.controller.WsAdHandlerV1
import ru.otus.otuskotlin.marketplace.springapp.api.v1.controller.WsOfferHandlerV1
import ru.otus.otuskotlin.marketplace.springapp.api.v2.controller.WsAdHandlerV2
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val adService: AdService,
    private val offerService: OfferService,
    private val objectMapper: ObjectMapper,
) : WebSocketConfigurer {

//    @Bean
//    fun sessions() = sessions

    private val sessions = mutableMapOf<String, SpringWsSession>()

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WsAdHandlerV1(adService, objectMapper, sessions), "/ws/ad/v1").setAllowedOrigins("*")
        registry.addHandler(WsAdHandlerV2(adService, sessions), "/ws/ad/v2").setAllowedOrigins("*")
        registry.addHandler(WsOfferHandlerV1(offerService, objectMapper, sessions), "/ws/offer/v1").setAllowedOrigins("*")
        registry.addHandler(WsOfferHandlerV2(offerService, sessions), "/ws/offer/v2").setAllowedOrigins("*")
    }
}
