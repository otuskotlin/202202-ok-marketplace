package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest

fun ICorChainDsl<MkplContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(adValidated)
        val result = adRepo.readAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adRepoRead = resultAd
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}
