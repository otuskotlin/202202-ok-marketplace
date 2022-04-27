package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.models.*
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdFilter

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: MkplAd = MkplAd(),
    var adFilterRequest: MkplAdFilter = MkplAdFilter(),
    var adResponse: MkplAd = MkplAd(),
    var adsResponse: MutableList<MkplAd> = mutableListOf(),
)
