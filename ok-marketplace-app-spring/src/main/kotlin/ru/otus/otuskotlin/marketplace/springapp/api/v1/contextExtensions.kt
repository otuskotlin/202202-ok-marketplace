package ru.otus.otuskotlin.marketplace.springapp.api.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.ResponseResult
import ru.otus.otuskotlin.marketplace.common.models.MkplError

fun buildError() = MkplError(
    field = "_", code = ResponseResult.ERROR.value
)


