package ru.otus.otuskotlin.marketplace.stubs.stubs

import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubResponse
import ru.otus.otuskotlin.marketplace.stubs.products.MkplAdStubBolts

object MkplAdStubCreateSuccess: MkplAdStub {
    override fun get() =  MkplAdStubResponse(
        ad = MkplAdStubBolts.AD_DEMAND_BOLT1
    )
}
