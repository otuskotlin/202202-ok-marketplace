package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.app.mappers.toModel
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.logging.MpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse>
        ApplicationCall.controllerHelperV1(
    logger: MpLogWrapper,
    logId: String,
    command: MkplCommand? = null,
    crossinline block: suspend MkplContext.() -> Unit
) {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
        principal = principal<JWTPrincipal>().toModel(),
    )
    try {
        logger.doWithLogging {
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            ctx.block()
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            val response = ctx.toTransportAd()
            respond(response)
        }
    } catch (e: Throwable) {
        command?.also { ctx.command = it }
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        logger.error(
            msg = "Fail to handle $command request",
            e = e,
            data = ctx.toLog("${logId}-error")
        )
        val response = ctx.toTransportAd()
        respond(response)
    }
}
