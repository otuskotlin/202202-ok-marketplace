package ru.otus.otuskotlin.marketplace.common.models.product

sealed interface IMkplAdProduct {
    fun deepCopy(): IMkplAdProduct
    companion object {
        val NONE = MkplAdProductNone
    }
}

object MkplAdProductNone: IMkplAdProduct {
    override fun deepCopy(): IMkplAdProduct = this
}
