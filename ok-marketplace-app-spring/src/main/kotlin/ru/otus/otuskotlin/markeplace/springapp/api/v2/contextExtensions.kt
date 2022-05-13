package ru.otus.otuskotlin.markeplace.springapp.api.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.ResponseResult
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun MkplContext.addError(error: () -> MkplError) =
    apply {
        state = MkplState.FAILING
        errors.add(error())
    }

fun buildError() = MkplError(
    field = "_", code = ResponseResult.ERROR.value
)