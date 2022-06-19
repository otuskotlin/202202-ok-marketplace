package ru.otus.otuskotlin.marketplace.springapp.api.v2.controller

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportRead
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession
import ru.otus.otuskotlin.marketplace.springapp.common.WsHandlerBase

@Component
class WsAdHandlerV2(
    private val adService: AdService,
    override val sessions: MutableMap<String, SpringWsSession>,
) : WsHandlerBase(sessions) {

    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val ctx = MkplContext(timeStart = Clock.System.now())
        val response = runBlocking {
            try {
                ctx.fromTransport(apiV2RequestDeserialize(message.payload) as IRequest)
                adService.exec(ctx)
                ctx.toTransportAd()
            } catch (e: Exception) {
                ctx.errors.add(e.asMkplError())
                ctx.toTransportRead()
            }
        }

        sessions.values.forEach {
            it.fwSession.sendMessage(TextMessage(apiV2ResponseSerialize(response)))
        }
    }
}
