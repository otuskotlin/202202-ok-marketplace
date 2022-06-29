package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest

fun ICorChainDsl<MkplContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdRequest(adValidated)
        val result = adRepo.updateAd(request)
        val resultAd = result.result
        if (result.isSuccess && resultAd != null) {
            adResponse = resultAd
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}
