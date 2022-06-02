package ru.otus.otuskotlin.marketplace.biz.workers

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

fun ICorChainDsl<MkplContext>.stubOffersSuccess(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.SUCCESS && state == MkplState.RUNNING }
    handle {
        state = MkplState.FINISHING
        adResponse = MkplAdStub.prepareResult {
            adRequest.id.takeIf { it != MkplAdId.NONE }?.also { this.id = it }
        }
        adsResponse.addAll(MkplAdStub.prepareOffersList(adResponse.title, MkplDealSide.SUPPLY))
    }
}
