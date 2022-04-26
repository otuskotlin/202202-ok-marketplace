package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.models.*
import kotlinx.datetime.Instant

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
//    var requestAd: Ad = Ad(),
)
