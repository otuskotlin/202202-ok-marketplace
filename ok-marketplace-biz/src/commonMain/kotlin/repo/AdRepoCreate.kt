package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest

fun ICorChainDsl<MkplContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdRequest(adRepoPrepare)
        val result = adRepo.createAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adRepoDone = resultAd
            state = MkplState.FINISHING
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}
