package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import ru.otus.otuskotlin.marketplace.springapp.api.v1.buildError
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession

@Component
class WsHandlerV1(
    private val adService: AdService,
    private val offerService: OfferService,
    private val objectMapper: ObjectMapper,
) : TextWebSocketHandler() {
    private val sessions = mutableMapOf<String, SpringWsSession>()

    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val response = when(val request = objectMapper.readValue<IRequest>(message.payload)) {
            is AdCreateRequest -> adService.createAd(MkplContext().apply { fromTransport(request) }).toTransportCreate()
            is AdReadRequest -> adService.readAd(MkplContext().apply { fromTransport(request) }, ::buildError).toTransportRead()
            is AdUpdateRequest -> adService.updateAd(MkplContext().apply { fromTransport(request) }, ::buildError).toTransportUpdate()
            is AdDeleteRequest -> adService.deleteAd(MkplContext().apply { fromTransport(request) }, ::buildError).toTransportDelete()
            is AdSearchRequest -> adService.searchAd(MkplContext().apply { fromTransport(request) }, ::buildError).toTransportSearch()
            is AdOffersRequest -> offerService.searchOffers(MkplContext().apply { fromTransport(request) }, ::buildError).toTransportOffers()
            else -> throw IllegalArgumentException("Illegal request type: ${request.javaClass}")
        }
        sessions.values.forEach {
            it.fwSession.sendMessage(TextMessage(objectMapper.writeValueAsString(response)))
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val clientSession = SpringWsSession(session)
        sessions[session.id] = clientSession
        session.sendMessage(TextMessage("Connection is established. Sessions: ${sessions.values}"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
