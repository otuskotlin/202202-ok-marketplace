package ru.otus.otuskotlin.marketplace.api.logs.mapper

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.logs.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*

fun MkplContext.toLog(logId: String) = CommonLogModel(
    messageId = uuid4().toString(),
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-marketplace",
    marketplace = toMkplLog(),
    errors = errors.map { it.toLog() },
)

fun MkplContext.toMkplLog():MkplLogModel? {
    val adNone = MkplAd()
    return MkplLogModel(
        requestId = requestId.takeIf { it != MkplRequestId.NONE }?.asString(),
        requestAd = adRequest.takeIf { it != adNone }?.toLog(),
        responseAd = adResponse.takeIf { it != adNone }?.toLog(),
        responseAds = adsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = adFilterRequest.takeIf { it != MkplAdFilter() }?.toLog(),
    ).takeIf { it != MkplLogModel() }
}

private fun MkplAdFilter.toLog() = AdFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
)

fun MkplError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() }
)

fun MkplAd.toLog() = AdLog(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    adType = adType.takeIf { it != MkplDealSide.NONE }?.name,
    visibility = visibility.takeIf { it != MkplVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
