package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model

import ru.otus.otuskotlin.marketplace.common.models.product.MkplLength
import ru.otus.otuskotlin.marketplace.common.models.product.MkplLengthUnits

data class MkplLengthEntity(
    val len: Double? = null,
    val unit: String? = null,
) {
    constructor(model: MkplLength): this(
        len = model.len.takeIf { it != 0.0 },
        unit = model.unit.takeIf { it != MkplLengthUnits.NONE }?.name,
    )

    fun toInternal() = MkplLength(
        len = len?: 0.0,
        unit = unit?.let { MkplLengthUnits.valueOf(it) }?: MkplLengthUnits.NONE,
    )
}
