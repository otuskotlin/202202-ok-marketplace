package ru.otus.otuskotlin.marketplace.common

import io.ktor.websocket.*
import ru.otus.otuskotlin.marketplace.common.models.IClientSession

data class KtorUserSession(
    override val fwSession: WebSocketSession
): IClientSession<WebSocketSession>
