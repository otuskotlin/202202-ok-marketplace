package ru.otus.otuskotlin.marketplace.springapp.api.v1.controller

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.logging.MpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

inline fun <reified Q : IRequest, reified R : IResponse>
        controllerHelperV1(
    request: Q,
    logger: MpLogWrapper,
    logId: String,
    command: MkplCommand? = null,
    noinline block: suspend MkplContext.() -> Unit,
): R = runBlocking {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    try {
        logger.doWithLogging {
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
            ctx.toTransportAd() as R
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
        ctx.toTransportAd() as R
    }
}
