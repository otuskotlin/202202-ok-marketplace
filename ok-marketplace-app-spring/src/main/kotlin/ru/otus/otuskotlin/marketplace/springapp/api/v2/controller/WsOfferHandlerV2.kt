package ru.otus.otuskotlin.marketplace.springapp.api.v2.controller

import kotlinx.datetime.Clock
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportOffers
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportRead
import ru.otus.otuskotlin.marketplace.springapp.api.v2.buildError
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession
import ru.otus.otuskotlin.marketplace.springapp.common.WsHandlerBase

class WsOfferHandlerV2(
    private val offerService: OfferService,
    override val sessions: MutableMap<String, SpringWsSession>
) : WsHandlerBase(sessions) {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val ctx = MkplContext(timeStart = Clock.System.now())
        val response = try {
            ctx.fromTransport(apiV2RequestDeserialize(message.payload) as IRequest)
            offerService.searchOffers(ctx, ::buildError).toTransportOffers()
        } catch (e: Exception) {
            ctx.errors.add(MkplError(exception = e))
            ctx.toTransportRead()
        }

        sessions.values.forEach {
            it.fwSession.sendMessage(TextMessage(apiV2ResponseSerialize(response)))
        }
    }
}
