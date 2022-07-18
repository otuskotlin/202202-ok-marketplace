package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.structure.T
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_AD_TYPE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLD_HEAD_STYLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_DIAMETER_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_DIAMETER_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_LENGTH_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_LENGTH_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_CONF
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_DESCRIPTION
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_LOCK
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_OWNER_ID
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_PRODUCT_TYPE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_TITLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_VISIBILITY
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.exceptions.WrongEnumException
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.models.product.*

fun Map<Any?, Any?>.toMkplAd(): MkplAd = MkplAd(
    id = (this[T.id] as? String)?.let { MkplAdId(it) } ?: MkplAdId.NONE,
    ownerId = (this[FIELD_OWNER_ID] as? String)?.let { MkplUserId(it) } ?: MkplUserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { MkplAdLock(it) } ?: MkplAdLock.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    description = (this[FIELD_DESCRIPTION] as? String) ?: "",
    adType = when (val value = this[FIELD_AD_TYPE] as? String) {
        MkplDealSide.SUPPLY.name -> MkplDealSide.SUPPLY
        MkplDealSide.DEMAND.name -> MkplDealSide.DEMAND
        null -> MkplDealSide.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "adType = '$value' cannot be converted to ${MkplDealSide::class}"
        )
    },
    visibility = when (val value = this[FIELD_VISIBILITY]) {
        MkplVisibility.VISIBLE_PUBLIC.name -> MkplVisibility.VISIBLE_PUBLIC
        MkplVisibility.VISIBLE_TO_GROUP.name -> MkplVisibility.VISIBLE_TO_GROUP
        MkplVisibility.VISIBLE_TO_OWNER.name -> MkplVisibility.VISIBLE_TO_OWNER
        null -> MkplVisibility.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "visibility = '$value' cannot be converted to ${MkplVisibility::class}"
        )
    },
    product = when (this[FIELD_PRODUCT_TYPE]) {
        MkplAdProductBolt::class.simpleName -> this@toMkplAd.toMkplAdProductBold()
        else -> MkplAdProductNone
    }
)

fun Map<Any?, Any?>.toMkplAdProductBold(): MkplAdProductBolt = MkplAdProductBolt(
    headStyle = when (val value = this[FIELD_BOLD_HEAD_STYLE]) {
        MkplAdProductBoltHeadStyle.HEXAGON_FLANGE.name -> MkplAdProductBoltHeadStyle.HEXAGON_FLANGE
        MkplAdProductBoltHeadStyle.INDENTED_HEXAGON.name -> MkplAdProductBoltHeadStyle.INDENTED_HEXAGON
        MkplAdProductBoltHeadStyle.INDENTED_HEXAGON_WASHER.name -> MkplAdProductBoltHeadStyle.INDENTED_HEXAGON_WASHER
        MkplAdProductBoltHeadStyle.SQUARE_SHOULDER.name -> MkplAdProductBoltHeadStyle.SQUARE_SHOULDER
        null -> MkplAdProductBoltHeadStyle.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "$FIELD_BOLD_HEAD_STYLE = '$value' cannot be converted to ${MkplAdProductBoltHeadStyle::class}"
        )
    },
    length = toMkplLength(FIELD_BOLT_LENGTH_LEN, FIELD_BOLT_LENGTH_UNIT),
    diameter = toMkplLength(FIELD_BOLT_DIAMETER_LEN, FIELD_BOLT_DIAMETER_UNIT),
    thread = MkplAdProductBoltThread(
        pitch = toMkplLength(FIELD_BOLT_THREAD_PITCH_LEN, FIELD_BOLT_THREAD_PITCH_UNIT),
        pitchConf = when (val value = this[FIELD_BOLT_THREAD_PITCH_CONF]) {
            MkplAdProductBoltThread.PitchConf.COARSE.name -> MkplAdProductBoltThread.PitchConf.COARSE
            MkplAdProductBoltThread.PitchConf.FINE.name -> MkplAdProductBoltThread.PitchConf.FINE
            null -> MkplAdProductBoltThread.PitchConf.FINE
            else -> throw WrongEnumException(
                "Cannot convert object from DB. " +
                        "$FIELD_BOLT_THREAD_PITCH_CONF = '$value' cannot be converted to ${MkplAdProductBoltThread.PitchConf::class}"
            )
        },
    )
)

fun Map<Any?, Any?>.toMkplLength(
    keyLen: String,
    keyUnit: String,
): MkplLength = Pair(
    this[keyLen] as? Double,
    when (val value = this[keyUnit]) {
        MkplLengthUnits.INCH.name -> MkplLengthUnits.INCH
        MkplLengthUnits.MILLIMETER.name -> MkplLengthUnits.MILLIMETER
        null -> MkplLengthUnits.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "$keyUnit = '$value' cannot be converted to ${MkplLengthUnits::class}"
        )
    }
).let {
    val len: Double = it.first ?: return@let null
    MkplLength(len, it.second)
} ?: MkplLength.NONE
