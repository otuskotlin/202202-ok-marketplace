package ru.otus.otuskotlin.marketplace.api.v1

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.KtorUserSession
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport

suspend fun WebSocketSession.mpWsHandlerV1(
    adService: AdService,
    offerService: OfferService,
    sessions: MutableSet<KtorUserSession>,
    objectMapper: ObjectMapper,
) {
    val userSession = KtorUserSession(this)
    sessions.add(userSession)

    try {
        run {
            val ctx = MkplContext(
                timeStart = Clock.System.now()
            )
            // обработка запроса на инициализацию
        }
        incoming
            .receiveAsFlow()
            .mapNotNull { it as? Frame.Text }
            .map { frame ->
                val jsonStr = frame.readText()
                objectMapper.readValue(jsonStr, IRequest::class.java)
            }
            .flowOn(Dispatchers.IO)
            .map { request ->
                val ctx = MkplContext(
                    timeStart = Clock.System.now()
                ).apply { fromTransport(request) }
                // обработка запроса
            }
            .collect()
    } catch (_: ClosedReceiveChannelException) {
        // Веб-сокет закрыт по инициативе клиента
    } finally {
        sessions.remove(userSession)
    }
}
