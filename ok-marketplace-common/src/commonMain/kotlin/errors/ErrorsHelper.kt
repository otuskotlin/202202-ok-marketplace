package ru.otus.otuskotlin.marketplace.common.errors

import ru.otus.otuskotlin.marketplace.common.models.MkplError

fun Throwable.asMkplError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MkplError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
