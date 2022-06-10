package ru.otus.otuskotlin.marketplace.api.v2

import io.ktor.websocket.*
import ru.otus.otuskotlin.marketplace.backend.services.AdService
import ru.otus.otuskotlin.marketplace.backend.services.OfferService
import ru.otus.otuskotlin.marketplace.common.KtorUserSession

suspend fun WebSocketSession.mpWsHandlerV2(
    adService: AdService,
    offerService: OfferService,
    sessions: MutableSet<KtorUserSession>
) {

}
