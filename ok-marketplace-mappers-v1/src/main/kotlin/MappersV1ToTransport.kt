package ru.otus.otuskotlin.marketplace.mappers.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.mappers.v1.ru.otus.otuskotlin.marketplace.mappers.v1.exceptions.UnknownMkplCommand

fun MkplContext.toTransportAds(): IResponse = when (val cmd = command) {
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
    ad = adResponse.toTransportAds()
)

fun MkplContext.toTransportRead() = AdReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAds()
)

fun MkplContext.toTransportUpdate() = AdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAds()
)

fun MkplContext.toTransportDelete() = AdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ad = adResponse.toTransportAds()
)

fun MkplContext.toTransportSearch() = AdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    ads = adsResponse.toTransportAds()
)

fun MkplContext.toTransportOffers() = AdOffersResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MkplState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    offers = adsResponse.toTransportAds()
)

fun List<MkplAd>.toTransportAds(): List<AdResponseObject>? = this
    .map { it.toTransportAds() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplAd.toTransportAds(): AdResponseObject = AdResponseObject(
    id = id.takeIf { it != MkplAdId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
    adType = adType.toTransportAds(),
    visibility = visibility.toTransportAds(),
    permissions = permissionsClient.toTransportAds(),
)

private fun Set<MkplAdPermissionClient>.toTransportAds(): Set<AdPermissions>? = this
    .map { it.toTransportAds() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MkplAdPermissionClient.toTransportAds() = when (this) {
    MkplAdPermissionClient.READ -> AdPermissions.READ
    MkplAdPermissionClient.UPDATE -> AdPermissions.UPDATE
    MkplAdPermissionClient.MAKE_VISIBLE_OWNER -> AdPermissions.MAKE_VISIBLE_OWN
    MkplAdPermissionClient.MAKE_VISIBLE_GROUP -> AdPermissions.MAKE_VISIBLE_GROUP
    MkplAdPermissionClient.MAKE_VISIBLE_PUBLIC -> AdPermissions.MAKE_VISIBLE_PUBLIC
    MkplAdPermissionClient.DELETE -> AdPermissions.DELETE
}

private fun MkplVisibility.toTransportAds(): AdVisibility? = when (this) {
    MkplVisibility.VISIBLE_PUBLIC -> AdVisibility.PUBLIC
    MkplVisibility.VISIBLE_TO_GROUP -> AdVisibility.REGISTERED_ONLY
    MkplVisibility.VISIBLE_TO_OWNER -> AdVisibility.OWNER_ONLY
    MkplVisibility.NONE -> null
}

private fun MkplDealSide.toTransportAds(): DealSide? = when (this) {
    MkplDealSide.DEMAND -> DealSide.DEMAND
    MkplDealSide.SUPPLY -> DealSide.PROPOSAL
    MkplDealSide.NONE -> null
}

private fun List<MkplError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAds() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MkplError.toTransportAds() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
