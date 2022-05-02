package ru.otus.otuskotlin.marketplace.mappers.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.models.product.*
import ru.otus.otuskotlin.marketplace.mappers.v2.exceptions.UnknownMkplCommand

fun MkplContext.toTransportAd(): IResponse = when (val cmd = command) {
    MkplCommand.CREATE -> toTransportCreate()
    MkplCommand.READ -> toTransportRead()
    MkplCommand.UPDATE -> toTransportRead()
    MkplCommand.DELETE -> toTransportRead()
    MkplCommand.SEARCH -> toTransportRead()
    MkplCommand.OFFERS -> toTransportRead()
    MkplCommand.NONE -> throw UnknownMkplCommand(cmd)
}

fun MkplContext.toTransportCreate() = AdCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportRead() = AdReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportUpdate() = AdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportDelete() = AdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAd()
)

fun MkplContext.toTransportSearch() = AdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAd()
)

fun MkplContext.toTransportOffers() = AdOffersResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    offers = adsResponse.toTransportAd()
)

fun List<MkplAd>.toTransportAd(): List<AdResponseObject>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplAd.toTransportAd(): AdResponseObject = AdResponseObject(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    adType = adType.toTransportAd(),
    visibility = visibility.toTransportAd(),
    permissions = permissionsClient.toTransportAd(),
    product = product.toTransport(),
)

private fun IMkplAdProduct.toTransport(): IAdProduct? = when(this) {
    IMkplAdProduct.NONE -> null
    MkplAdProductNone -> null
    is MkplAdProductBolt -> this.toTransport()
}

private fun MkplAdProductBolt.toTransport() = AdProductBolt(
    length = this.length.len,
    diameter = this.diameter.len,
    headStyle = this.headStyle.toTransport(),
    thread = this.thread.toTransport()
)

private fun MkplAdProductBoltHeadStyle.toTransport() = when(this) {
    MkplAdProductBoltHeadStyle.SQUARE_SHOULDER -> AdProductBolt.HeadStyle.SQUARE_SHOULDER
    MkplAdProductBoltHeadStyle.INDENTED_HEXAGON_WASHER -> AdProductBolt.HeadStyle.INDENTED_HEXAGON_WASHER
    MkplAdProductBoltHeadStyle.INDENTED_HEXAGON -> AdProductBolt.HeadStyle.INDENTED_HEXAGON
    MkplAdProductBoltHeadStyle.HEXAGON_FLANGE -> AdProductBolt.HeadStyle.HEXAGON_FLANGE
    MkplAdProductBoltHeadStyle.NONE -> null
}

private fun MkplAdProductBoltThread.toTransport() = AdProductBoltThread(
    pitch = this.pitch.len,
    pitchConf = this.pitchConf.toTransport()
)

private fun MkplAdProductBoltThread.PitchConf.toTransport(): AdProductBoltThread.PitchConf? = when(this) {
    MkplAdProductBoltThread.PitchConf.COARSE -> AdProductBoltThread.PitchConf.COARSE
    MkplAdProductBoltThread.PitchConf.FINE -> AdProductBoltThread.PitchConf.FINE
    MkplAdProductBoltThread.PitchConf.NONE -> null
}

private fun Set<MkplAdPermissionClient>.toTransportAd(): Set<AdPermissions>? = this
    .map { it.toTransportAd() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplAdPermissionClient.toTransportAd() = when (this) {
    MkplAdPermissionClient.READ -> AdPermissions.READ
    MkplAdPermissionClient.UPDATE -> AdPermissions.UPDATE
    MkplAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissions.MAKE_VISIBLE_OWN
    MkplAdPermissionClient.MAKE_VISIBLE_GROUP -> AdPermissions.MAKE_VISIBLE_GROUP
    MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissions.MAKE_VISIBLE_PUBLIC
    MkplAdPermissionClient.DELETE -> AdPermissions.DELETE
}

private fun MkplVisibility.toTransportAd(): AdVisibility? = when (this) {
    MkplVisibility.VISIBLE_PUBLIC -> AdVisibility.PUBLIC
    MkplVisibility.VISIBLE_TO_GROUP -> AdVisibility.REGISTERED_ONLY
    MkplVisibility.VISIBLE_TO_OWNER -> AdVisibility.OWNER_ONLY
    MkplVisibility.NONE -> null
}

private fun MkplDealSide.toTransportAd(): DealSide? = when (this) {
    MkplDealSide.DEMAND -> DealSide.DEMAND
    MkplDealSide.SUPPLY -> DealSide.SUPPLY
    MkplDealSide.NONE -> null
}

private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
