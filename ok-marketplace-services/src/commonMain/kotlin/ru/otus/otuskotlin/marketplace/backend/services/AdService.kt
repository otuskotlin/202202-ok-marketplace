package ru.otus.otuskotlin.marketplace.backend.services

import ru.otus.otuskotlin.marketplace.stubs.products.Bolt
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs

class AdService {
    fun createAd(mpContext: MkplContext): MkplContext {
        val response = when (mpContext.workMode) {
            MkplWorkMode.PROD -> TODO()
            MkplWorkMode.TEST -> mpContext.adRequest
            MkplWorkMode.STUB -> Bolt.getModel()
        }
        return mpContext.successResponse {
            adResponse = response
        }
    }

    fun readAd(mpContext: MkplContext, buildError: () -> MkplError): MkplContext {
        val requestedId = mpContext.adRequest.id

        return when (mpContext.stubCase) {
            MkplStubs.SUCCESS -> mpContext.successResponse {
                adResponse = Bolt.getModel().apply { id = requestedId }
            }
            else -> mpContext.errorResponse(buildError) {
                it.copy(field = "ad.id", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun updateAd(context: MkplContext, buildError: () -> MkplError) = when (context.stubCase) {
        MkplStubs.SUCCESS -> context.successResponse {
            adResponse = Bolt.getModel {
                if (adRequest.visibility != MkplVisibility.NONE) visibility = adRequest.visibility
                if (adRequest.id != MkplAdId.NONE) id = adRequest.id
                if (adRequest.title.isNotEmpty()) title = adRequest.title
            }
        }
        else -> context.errorResponse(buildError) {
            it.copy(field = "ad.id", message = notFoundError(context.adRequest.id.asString()))
        }
    }


    fun deleteAd(context: MkplContext, buildError: () -> MkplError): MkplContext = when (context.stubCase) {
        MkplStubs.SUCCESS -> context.successResponse {
            adResponse = Bolt.getModel { id = context.adRequest.id }
        }
        else -> context.errorResponse(buildError) {
            it.copy(
                field = "ad.id",
                message = notFoundError(context.adRequest.id.asString())
            )
        }
    }

    fun searchAd(context: MkplContext, buildError: () -> MkplError): MkplContext {
        val filter = context.adFilterRequest

        val searchableString = filter.searchString

        return when (context.stubCase) {
            MkplStubs.SUCCESS -> context.successResponse {
                adsResponse.addAll(
                    Bolt.getModels()
                )
            }
            else -> context.errorResponse(buildError) {
                it.copy(
                    message = "Nothing found by $searchableString"
                )
            }
        }
    }
}
