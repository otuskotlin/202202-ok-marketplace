package ru.otus.otuskotlin.marketplace.biz

import com.crowdproj.kotlin.cor.rootChain
import ru.otus.otuskotlin.marketplace.common.MkplContext

class MkplAdProcessor() {
    suspend fun exec(ctx: MkplContext) = BuzinessChain.exec(ctx)

    companion object {
        private val BuzinessChain = rootChain<MkplContext> {

        }.build()
    }
}
