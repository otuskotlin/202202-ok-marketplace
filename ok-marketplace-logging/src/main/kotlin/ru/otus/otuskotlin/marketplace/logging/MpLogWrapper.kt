package ru.otus.otuskotlin.marketplace.logging

import ch.qos.logback.classic.Logger
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent
import java.time.Instant

class MpLogWrapper (
    /**
     * Экземпляр логера (Logback)
     */
    val logger: Logger,
    /**
     * Идентификатор логера. Пробрасывается в Logback и замещает [[loggerClass]]. Также используется в сообщения
     * логера о входе и выходе из функции.
     */
    val loggerId: String = ""
) {
    /**
     * Основная функция для логирования
     */
    fun log(
        msg: String = "",
        level: Level = Level.TRACE,
        marker: Marker = DefaultMarker("DEV"),
        e: Throwable? = null,
        data: Any? = null,
        vararg objs: Pair<String, Any>?
    ) {
        logger.log(object : LoggingEvent {
            override fun getThrowable() = e
            override fun getTimeStamp(): Long = Instant.now().toEpochMilli()
            override fun getThreadName(): String = Thread.currentThread().name
            override fun getMessage(): String = msg
            override fun getMarker(): Marker = marker
            override fun getArgumentArray(): Array<out Any> = data?.let { d ->
                arrayOf(
                    *objs.map { StructuredArguments.keyValue(it?.first, it?.second) }.toTypedArray(),
                    StructuredArguments.keyValue("data", d),
                ).filterNotNull().toTypedArray()
            } ?: objs.mapNotNull { StructuredArguments.keyValue(it?.first, it?.second) }.toTypedArray()

            override fun getLevel(): Level = level
            override fun getLoggerName(): String = logger.name
        })
    }

    fun error(
        msg: String = "",
        marker: Marker = DefaultMarker("DEV"),
        e: Throwable? = null,
        data: Any? = null,
        vararg objs: Pair<String, Any>?
    ) = log(msg, Level.ERROR, marker, e, data, *objs)

    fun info(
        msg: String = "",
        marker: Marker = DefaultMarker("DEV"),
        data: Any? = null,
        vararg objs: Pair<String, Any>?
    ) = log(msg, Level.INFO, marker, null, data, *objs)

    /**
     * Функция обертка для выполнения прикладного кода с логированием перед выполнением и после
     */
    suspend fun <T> doWithLogging(
        id: String = "",
        level: Level = Level.INFO,
        marker: Marker = DefaultMarker("DEV"),
        block: suspend () -> T,
    ): T = try {
        val timeStart = Instant.now()
        log("$loggerId Entering $id", level)
        block().also {
            val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
            log(
                msg = "$loggerId Finishing $id",
                level = level,
                marker = marker,
                objs = arrayOf(Pair("metricHandleTime", diffTime))
            )
        }
    } catch (e: Throwable) {
        log(
            msg = "$loggerId Failing $id",
            level = Level.ERROR,
            e = e)
        throw e
    }

    /**
     * Функция обертка для выполнения прикладного кода с логированием ошибки
     */
    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log(
            msg = "$loggerId Failing $id",
            level = Level.ERROR,
            e = e)
        if (throwRequired) throw e else null
    }
}
