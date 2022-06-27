package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model

import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBoltThread
import ru.otus.otuskotlin.marketplace.common.models.product.MkplLength

data class MkplAdProductBoltThreadEntity(
    val pitch: MkplLengthEntity? = null,
    val pitchConf: String? = null,
) {
    constructor(model: MkplAdProductBoltThread): this(
        pitch = model.pitch.takeIf { it != MkplLength.NONE }?.let { MkplLengthEntity(it) },
        pitchConf = model.pitchConf.takeIf { it != MkplAdProductBoltThread.PitchConf.NONE }?.name,
    )

    fun toInternal() = MkplAdProductBoltThread(
        pitch = pitch?.toInternal()?: MkplLength.NONE,
        pitchConf = pitchConf?.let { MkplAdProductBoltThread.PitchConf.valueOf(it) }?: MkplAdProductBoltThread.PitchConf.NONE,
    )
}
