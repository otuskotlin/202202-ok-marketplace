package ru.otus.otuskotlin.marketplace.backend.services

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.exceptions.UnknownMkplCommand
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs

class AdService {
    private val processor = MkplAdProcessor()

    fun handleAd(mpContext: MkplContext, buildError: () -> MkplError) = when(val cmd = mpContext.command) {
        MkplCommand.CREATE -> createAd(mpContext)
        MkplCommand.READ -> readAd(mpContext, buildError)
        MkplCommand.UPDATE -> updateAd(mpContext, buildError)
        MkplCommand.DELETE -> deleteAd(mpContext, buildError)
        MkplCommand.SEARCH -> searchAd(mpContext, buildError)
        else -> throw UnknownMkplCommand(cmd)
    }


    suspend fun exec(context: MkplContext) = processor.exec(context)

    suspend fun createAd(context: MkplContext) = processor.exec(context)
    suspend fun readAd(context: MkplContext) = processor.exec(context)
    suspend fun updateAd(context: MkplContext) = processor.exec(context)
    suspend fun deleteAd(context: MkplContext) = processor.exec(context)
    suspend fun searchAd(context: MkplContext) = processor.exec(context)
    suspend fun searchOffers(context: MkplContext) = processor.exec(context)
}
