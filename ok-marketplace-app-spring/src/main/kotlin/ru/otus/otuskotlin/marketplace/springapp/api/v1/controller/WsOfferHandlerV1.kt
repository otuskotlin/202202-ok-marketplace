package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.datetime.Clock
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportRead
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportOffers
import ru.otus.otuskotlin.marketplace.springapp.api.v1.buildError
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession
import ru.otus.otuskotlin.marketplace.springapp.common.WsHandlerBase

class WsOfferHandlerV1(
    private val offerService: OfferService,
    private val objectMapper: ObjectMapper,
    override val sessions: MutableMap<String, SpringWsSession>
) : WsHandlerBase(sessions) {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val ctx = MkplContext(timeStart = Clock.System.now())
        val response = try {
            ctx.fromTransport(objectMapper.readValue<IRequest>(message.payload))
            offerService.searchOffers(ctx, ::buildError).toTransportOffers()
        } catch (e: Exception) {
            ctx.errors.add(MkplError(exception = e))
            ctx.toTransportRead()
        }

        sessions.values.forEach {
            it.fwSession.sendMessage(TextMessage(objectMapper.writeValueAsString(response)))
        }
    }
}
