package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.structure.T
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.exceptions.WrongEnumException
import ru.otus.otuskotlin.marketplace.common.models.*

fun MutableMap<Any?, Any?>.toMkplAd(): MkplAd = MkplAd(
    id = (this[T.id] as? String)?.let { MkplAdId(it) } ?: MkplAdId.NONE,
    ownerId = (this["ownerId"] as? String)?.let { MkplUserId(it) } ?: MkplUserId.NONE,
    lock = (this["lock"] as? String)?.let { MkplAdLock(it) } ?: MkplAdLock.NONE,
    title = (this["title"] as? String) ?: "",
    description = (this["description"] as? String) ?: "",
    adType = when (val value = this["adType"] as? String) {
        MkplDealSide.SUPPLY.name -> MkplDealSide.SUPPLY
        MkplDealSide.DEMAND.name -> MkplDealSide.DEMAND
        null -> MkplDealSide.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "adType = '$value' cannot be converted to ${MkplDealSide::class}"
        )
    },
    visibility = when (val value = this["visibility"]) {
        MkplVisibility.VISIBLE_PUBLIC.name -> MkplVisibility.VISIBLE_PUBLIC
        MkplVisibility.VISIBLE_TO_GROUP.name -> MkplVisibility.VISIBLE_TO_GROUP
        MkplVisibility.VISIBLE_TO_OWNER.name -> MkplVisibility.VISIBLE_TO_OWNER
        null -> MkplVisibility.NONE
        else -> throw WrongEnumException(
            "Cannot convert object from DB. " +
                    "visibility = '$value' cannot be converted to ${MkplVisibility::class}"
        )
    },
)
