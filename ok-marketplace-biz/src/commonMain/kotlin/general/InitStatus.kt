package ru.otus.otuskotlin.marketplace.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == MkplState.NONE }
    handle { state = MkplState.RUNNING }
}
