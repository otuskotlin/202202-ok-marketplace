package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.models.product.MkplLength
import ru.otus.otuskotlin.marketplace.common.models.product.MkplLengthUnits

val Number.mm: MkplLength
    get() = MkplLength(len = toDouble(), unit = MkplLengthUnits.MILLIMETER)

val Number.inch: MkplLength
    get() = MkplLength(len = toDouble(), unit = MkplLengthUnits.INCH)
