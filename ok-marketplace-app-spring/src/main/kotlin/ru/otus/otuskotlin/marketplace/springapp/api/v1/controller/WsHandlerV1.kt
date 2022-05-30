package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WsHandlerV1 : TextWebSocketHandler() {

    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        session.sendMessage(TextMessage("Your message: $payload"))
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.sendMessage(TextMessage("Connection is established ${session.id}"))
    }
}
