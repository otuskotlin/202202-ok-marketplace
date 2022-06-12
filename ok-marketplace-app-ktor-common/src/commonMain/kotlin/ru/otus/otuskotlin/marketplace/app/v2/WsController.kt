package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportRead

suspend fun WebSocketSession.mpWsHandlerV2(
    service: AdService,
    sessions: MutableSet<KtorUserSession>
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)
        run {
            val ctx = MkplContext(
                timeStart = Clock.System.now()
            )
            // обработка запроса на инициализацию
            outgoing.send(Frame.Text(apiV2ResponseSerialize(ctx.toTransportRead())))
        }
        incoming
            .receiveAsFlow()
            .mapNotNull { it as? Frame.Text }
            .map { frame ->
                val ctx = MkplContext(
                    timeStart = Clock.System.now()
                )
                // Обработка исключений без завершения flow
                try {
                    val jsonStr = frame.readText()
                    val request = apiV2RequestDeserialize<IRequest>(jsonStr)
                    ctx.fromTransport(request)
                    service.exec(ctx)
                    outgoing.send(Frame.Text(apiV2ResponseSerialize(ctx.toTransportAd())))
                } catch (_: ClosedReceiveChannelException) {
                    sessions.remove(userSession)
                } catch (t: Throwable) {
                    ctx.addError(
                        MkplError(exception = t)
                    )
                    outgoing.send(Frame.Text(apiV2ResponseSerialize(ctx.toTransportRead())))
                }
            }
            .collect()
}

