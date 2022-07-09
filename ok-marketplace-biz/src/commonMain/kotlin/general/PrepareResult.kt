package ru.otus.otuskotlin.marketplace.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode

fun ICorChainDsl<MkplContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MkplWorkMode.STUB }
    handle {
        adResponse = adRepoDone
        adsResponse = adsRepoDone
        state = when (val st = state) {
            MkplState.RUNNING -> MkplState.FINISHING
            else -> st
        }
    }
}
