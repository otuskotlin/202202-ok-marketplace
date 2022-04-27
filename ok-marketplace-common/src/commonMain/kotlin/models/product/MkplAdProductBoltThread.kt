package ru.otus.otuskotlin.marketplace.common.models.product

data class MkplAdProductBoltThread(
    val pitch: MkplLength = MkplLength.NONE,
    val pitchConf: PitchConf = PitchConf.NONE,
) {
    enum class PitchConf {
        NONE,
        COARSE,
        FINE,
    }
    companion object {
        val NONE = MkplAdProductBoltThread()
    }
}
