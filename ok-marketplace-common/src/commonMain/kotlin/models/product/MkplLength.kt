package ru.otus.otuskotlin.marketplace.common.models.product

data class MkplLength(
    val len: Double = 0.0,
    val unit: MkplLengthUnits = MkplLengthUnits.NONE,
) {
    companion object {
        val NONE = MkplLength()
    }
}
