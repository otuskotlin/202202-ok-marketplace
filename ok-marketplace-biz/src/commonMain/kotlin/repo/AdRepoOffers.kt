package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest

fun ICorChainDsl<MkplContext>.repoOffers(title: String) = worker {
    description = "Поиск предложений для объявления по названию"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(requestId)
        val result = adRepo.searchOffers(request)
        val resultAds = result.result
        if (result.isSuccess && resultAds != null) {
            adsResponse = resultAds.toMutableList()
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}
