package ru.otus.otuskotlin.marketplace.springapp.common

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

abstract class WsHandlerBase(
    protected open val sessions: MutableMap<String, SpringWsSession>,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val clientSession = SpringWsSession(session)
        sessions[session.id] = clientSession
        session.sendMessage(TextMessage("Connection is established. Session ID: ${session.id}"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
