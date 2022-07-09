package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState

fun ICorChainDsl<MkplContext>.repoPrepareOffers(title: String) = worker {
    this.title = title
    description = "Готовим данные к поиску предложений в БД"
    on { state == MkplState.RUNNING }
    handle {
        adRepoPrepare = adRepoRead.deepCopy()
        adRepoDone = adRepoRead.deepCopy()
    }
}
