package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.app.v2.KtorUserSession
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportRead

suspend fun WebSocketSession.mpWsHandlerV1(
    service: AdService,
    sessions: MutableSet<KtorUserSession>,
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)

    run {
        val ctx = MkplContext(
            timeStart = Clock.System.now()
        )
        // обработка запроса на инициализацию
        outgoing.send(Frame.Text(apiV1ResponseSerialize(ctx.toTransportRead())))
    }
    incoming
        .receiveAsFlow()
        .mapNotNull { it as? Frame.Text }
        .map { frame ->
            val jsonStr = frame.readText()
            apiV2RequestDeserialize<IRequest>(jsonStr)
        }
        .flowOn(Dispatchers.IO)
        .map { request ->
            val ctx = MkplContext(
                timeStart = Clock.System.now()
            ).apply { fromTransport(request) }
            service.exec(ctx)
            ctx
        }
        .flowOn(Dispatchers.Default)
        .map {
            outgoing.send(Frame.Text(apiV1ResponseSerialize(it.toTransportAd())))
        }
        .flowOn(Dispatchers.IO)
            // Обработка исключений с завершением flow
        .catch { e ->
            if (e is ClosedReceiveChannelException) { sessions.remove(userSession) }
            else {
                val ctx = MkplContext(
                    timeStart = Clock.System.now()
                )
                ctx.addError(e.asMkplError())
                outgoing.send(Frame.Text(apiV1ResponseSerialize(ctx.toTransportRead())))
                userSession.fwSession.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ""))
                sessions.remove(userSession)
            }
        }
        .collect()
}
