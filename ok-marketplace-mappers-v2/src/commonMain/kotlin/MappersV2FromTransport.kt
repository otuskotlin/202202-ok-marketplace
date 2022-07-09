package ru.otus.otuskotlin.marketplace.mappers.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.mm
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.models.product.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.mappers.v2.exceptions.UnknownAdProduct
import ru.otus.otuskotlin.marketplace.mappers.v2.exceptions.UnknownRequestClass

fun MkplContext.fromTransport(request: IRequest) = when(request){
    is AdCreateRequest -> fromTransport(request)
    is AdReadRequest -> fromTransport(request)
    is AdUpdateRequest -> fromTransport(request)
    is AdDeleteRequest -> fromTransport(request)
    is AdSearchRequest-> fromTransport(request)
    is AdOffersRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

private fun String?.toAdId() = this?.let { MkplAdId(it) } ?: MkplAdId.NONE
private fun String?.toAdLock() = this?.let { MkplAdLock(it) } ?: MkplAdLock.NONE
private fun AdIdObject?.toAdWithId() = MkplAd(id = this?.id.toAdId())
private fun IRequest?.requestId() = this?.requestId?.let { MkplRequestId(it) } ?: MkplRequestId.NONE

private fun AdDebug?.transportToWorkMode(): MkplWorkMode = when(this?.mode) {
    AdRequestDebugMode.PROD -> MkplWorkMode.PROD
    AdRequestDebugMode.TEST -> MkplWorkMode.TEST
    AdRequestDebugMode.STUB -> MkplWorkMode.STUB
    null -> MkplWorkMode.PROD
}

private fun AdDebug?.transportToStubCase(): MkplStubs = when(this?.stub) {
    AdRequestDebugStubs.SUCCESS -> MkplStubs.SUCCESS
    AdRequestDebugStubs.NOT_FOUND -> MkplStubs.NOT_FOUND
    AdRequestDebugStubs.BAD_ID -> MkplStubs.BAD_ID
    AdRequestDebugStubs.BAD_TITLE -> MkplStubs.BAD_TITLE
    AdRequestDebugStubs.BAD_DESCRIPTION -> MkplStubs.BAD_DESCRIPTION
    AdRequestDebugStubs.BAD_VISIBILITY -> MkplStubs.BAD_VISIBILITY
    AdRequestDebugStubs.CANNOT_DELETE -> MkplStubs.CANNOT_DELETE
    AdRequestDebugStubs.BAD_SEARCH_STRING -> MkplStubs.BAD_SEARCH_STRING
    null -> MkplStubs.NONE
}

fun MkplContext.fromTransport(request: AdCreateRequest) {
    command = MkplCommand.CREATE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: MkplAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdReadRequest) {
    command = MkplCommand.READ
    requestId = request.requestId()
    adRequest = request.ad.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdUpdateRequest) {
    command = MkplCommand.UPDATE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: MkplAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdDeleteRequest) {
    command = MkplCommand.DELETE
    requestId = request.requestId()
    adRequest = request.ad?.toInternal() ?: MkplAd()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdSearchRequest) {
    command = MkplCommand.SEARCH
    requestId = request.requestId()
    adFilterRequest = request.adFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MkplContext.fromTransport(request: AdOffersRequest) {
    command = MkplCommand.OFFERS
    requestId = request.requestId()
    adRequest = request.ad.toAdWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun AdSearchFilter?.toInternal(): MkplAdFilter = MkplAdFilter(
    searchString = this?.searchString ?: ""
)

private fun AdCreateObject.toInternal(): MkplAd = MkplAd(
    title = this.title ?: "",
    description = this.description ?: "",
    adType = this.adType.fromTransport(), // HelperMMapper.fromTransport(reuest)
    visibility = this.visibility.fromTransport(),
    product = this.product.fromTransport(),
)

private fun AdUpdateObject.toInternal(): MkplAd = MkplAd(
    id = this.id.toAdId(),
    title = this.title ?: "",
    description = this.description ?: "",
    adType = this.adType.fromTransport(),
    visibility = this.visibility.fromTransport(),
    product = this.product.fromTransport()
)

private fun AdDeleteObject.toInternal(): MkplAd = MkplAd(
    id = this.id.toAdId(),
    lock = this.lock.toAdLock(),
)

private fun AdVisibility?.fromTransport(): MkplVisibility = when(this) {
    AdVisibility.PUBLIC -> MkplVisibility.VISIBLE_PUBLIC
    AdVisibility.OWNER_ONLY -> MkplVisibility.VISIBLE_TO_OWNER
    AdVisibility.REGISTERED_ONLY -> MkplVisibility.VISIBLE_TO_GROUP
    null -> MkplVisibility.NONE
}

private fun DealSide?.fromTransport(): MkplDealSide = when(this) {
    DealSide.DEMAND -> MkplDealSide.DEMAND
    DealSide.SUPPLY -> MkplDealSide.SUPPLY
    null -> MkplDealSide.NONE
}

private fun IAdProduct?.fromTransport(): IMkplAdProduct = when(val prod = this) {
    null -> IMkplAdProduct.NONE
    is AdProductBolt -> prod.fromTransport()
    else -> throw UnknownAdProduct(prod)
}

private fun AdProductBolt.fromTransport() = MkplAdProductBolt(
    length = this.length?.mm ?: MkplLength.NONE,
    diameter = this.diameter?.mm ?: MkplLength.NONE,
    headStyle = this.headStyle.fromTransport(),
    thread = this.thread.fromTransport(),
)

private fun AdProductBolt.HeadStyle?.fromTransport(): MkplAdProductBoltHeadStyle = when(this) {
    AdProductBolt.HeadStyle.HEXAGON_FLANGE -> MkplAdProductBoltHeadStyle.HEXAGON_FLANGE
    AdProductBolt.HeadStyle.INDENTED_HEXAGON -> MkplAdProductBoltHeadStyle.INDENTED_HEXAGON
    AdProductBolt.HeadStyle.INDENTED_HEXAGON_WASHER -> MkplAdProductBoltHeadStyle.INDENTED_HEXAGON_WASHER
    AdProductBolt.HeadStyle.SQUARE_SHOULDER -> MkplAdProductBoltHeadStyle.SQUARE_SHOULDER
    null -> MkplAdProductBoltHeadStyle.NONE
}

private fun AdProductBoltThread?.fromTransport(): MkplAdProductBoltThread = MkplAdProductBoltThread(
    pitch = this?.pitch?.mm ?: MkplLength.NONE,
    pitchConf = this?.pitchConf.fromTransport()
)

private fun AdProductBoltThread.PitchConf?.fromTransport(): MkplAdProductBoltThread.PitchConf = when(this) {
    AdProductBoltThread.PitchConf.COARSE -> MkplAdProductBoltThread.PitchConf.COARSE
    AdProductBoltThread.PitchConf.FINE -> MkplAdProductBoltThread.PitchConf.FINE
    null -> MkplAdProductBoltThread.PitchConf.NONE
}
