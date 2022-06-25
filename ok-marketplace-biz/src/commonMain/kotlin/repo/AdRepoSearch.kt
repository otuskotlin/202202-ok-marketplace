package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest

fun ICorChainDsl<MkplContext>.repoSearch(title: String) = worker {
    description = "Поиск объявлений в БД по фильтру"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdFilterRequest(
            titleFilter = adFilterRequest.searchString,
            ownerId = adFilterRequest.ownerId,
            dealSide = adFilterRequest.dealSide,
        )
        val result = adRepo.searchAd(request)
        val resultAds = result.result
        if (result.isSuccess && resultAds != null) {
            adsResponse = resultAds.toMutableList()
        } else {
            state = MkplState.FAILING
            errors.addAll(result.errors)
        }
    }
}
