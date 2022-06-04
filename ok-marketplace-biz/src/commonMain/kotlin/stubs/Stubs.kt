package ru.otus.otuskotlin.marketplace.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode

fun ICorChainDsl<MkplContext>.stubs(title: String, block: ICorChainDsl<MkplContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MkplWorkMode.STUB && state == MkplState.RUNNING }
}
