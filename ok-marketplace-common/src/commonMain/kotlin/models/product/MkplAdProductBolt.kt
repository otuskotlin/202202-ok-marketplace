package ru.otus.otuskotlin.marketplace.common.models.product

data class MkplAdProductBolt(
    var length: MkplLength = MkplLength.NONE,
    var diameter: MkplLength = MkplLength.NONE,
    var headStyle: MkplAdProductBoltHeadStyle = MkplAdProductBoltHeadStyle.NONE,
    var thread: MkplAdProductBoltThread = MkplAdProductBoltThread.NONE,
): IMkplAdProduct
