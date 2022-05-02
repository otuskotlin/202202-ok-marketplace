package ru.otus.otuskotlin.marketplace.common.models.product

sealed interface IMkplAdProduct {
    companion object {
        val NONE = MkplAdProductNone
    }
}

object MkplAdProductNone: IMkplAdProduct
