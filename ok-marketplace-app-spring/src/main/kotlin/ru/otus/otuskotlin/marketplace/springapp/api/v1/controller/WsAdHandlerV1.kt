package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportRead
import ru.otus.otuskotlin.marketplace.springapp.common.SpringWsSession
import ru.otus.otuskotlin.marketplace.springapp.common.WsHandlerBase

@Component
class WsAdHandlerV1(
    private val adService: AdService,
    private val objectMapper: ObjectMapper,
    override val sessions: MutableMap<String, SpringWsSession>,
) : WsHandlerBase(sessions) {
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val ctx = MkplContext(timeStart = Clock.System.now())
        val response = runBlocking {
            try {
                ctx.fromTransport(objectMapper.readValue<IRequest>(message.payload))
                adService.exec(ctx)
                ctx.toTransportAd()
            } catch (e: Exception) {
                ctx.errors.add(MkplError(exception = e))
                ctx.toTransportRead()
            }
        }

        sessions.values.forEach {
            it.fwSession.sendMessage(TextMessage(objectMapper.writeValueAsString(response)))
        }
    }
}
