package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model

import ru.otus.otuskotlin.marketplace.common.models.product.*

sealed interface IMkplAdProductEntity {

}

data class MkplAdProductBoltEntity(
    val length: MkplLengthEntity? = null,
    val diameter: MkplLengthEntity? = null,
    val headStyle: String? = null,
    val thread: MkplAdProductBoltThreadEntity? = null,
): IMkplAdProductEntity {
    constructor(model: MkplAdProductBolt): this(
        length = model.length.takeIf { it != MkplLength.NONE }?.let { MkplLengthEntity(it) },
        diameter = model.diameter.takeIf { it != MkplLength.NONE }?.let { MkplLengthEntity(it) },
        headStyle = model.headStyle.takeIf { it != MkplAdProductBoltHeadStyle.NONE }?.name,
        thread = model.thread.takeIf { it != MkplAdProductBoltThread.NONE }?.let { MkplAdProductBoltThreadEntity(it) },
    )

    fun toInternal() = MkplAdProductBolt(
        length = length?.toInternal()?: MkplLength.NONE,
        diameter = diameter?.toInternal()?: MkplLength.NONE,
        headStyle = headStyle?.let { MkplAdProductBoltHeadStyle.valueOf(it) }?: MkplAdProductBoltHeadStyle.NONE,
        thread = thread?.toInternal()?: MkplAdProductBoltThread.NONE,
    )
}

fun IMkplAdProductEntity?.toInternal() = when(this) {
    is MkplAdProductBoltEntity -> this.toInternal()
    null -> IMkplAdProduct.NONE
}

internal fun IMkplAdProduct.toEntity() = when(this) {
    is MkplAdProductBolt -> MkplAdProductBoltEntity(this)
    IMkplAdProduct.NONE -> null
    MkplAdProductNone -> null
}
