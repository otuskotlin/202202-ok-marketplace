package ru.otus.otuskotlin.marketplace.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        adValidated = adValidating
    }
}

fun ICorChainDsl<MkplContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        adFilterValidated = adFilterValidating
    }
}
