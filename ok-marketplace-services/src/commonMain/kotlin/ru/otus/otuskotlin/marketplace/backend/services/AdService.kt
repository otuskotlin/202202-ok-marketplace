package ru.otus.otuskotlin.marketplace.backend.services

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class AdService(
    val adRepositoty: IAdRepository
) {
    private val processor = MkplAdProcessor()

    suspend fun exec(context: MkplContext) = processor.exec(context.applySettings())

    suspend fun createAd(context: MkplContext) = processor.exec(context.applySettings())
    suspend fun readAd(context: MkplContext) = processor.exec(context.applySettings())
    suspend fun updateAd(context: MkplContext) = processor.exec(context.applySettings())
    suspend fun deleteAd(context: MkplContext) = processor.exec(context.applySettings())
    suspend fun searchAd(context: MkplContext) = processor.exec(context.applySettings())
    suspend fun searchOffers(context: MkplContext) = processor.exec(context.applySettings())

    private fun MkplContext.applySettings() = apply {
        adRepo = adRepositoty
    }
}
