package ru.otus.otuskotlin.marketplace.stubs.stubs

import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubResponse

sealed interface MkplAdStub {
    fun get(): MkplAdStubResponse

    fun prepareResult(block: MkplAd.() -> Unit): MkplAdStubResponse = get()
        .apply {
            ad.block()
        }
}
