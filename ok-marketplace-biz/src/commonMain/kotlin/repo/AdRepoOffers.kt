package ru.otus.otuskotlin.marketplace.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponse

fun ICorChainDsl<MkplContext>.repoOffers(title: String) = worker {
    this.title = title
    description = "Поиск предложений для объявления по названию"
    on { state == MkplState.RUNNING }
    handle {
        val request = DbAdIdRequest(adValidated.id)
        val adResponse = adRepo.readAd(request)
        val adRequest = adResponse.takeIf { it.isSuccess && it.result != null }?.result

        /**
         * Формирование фильтра по заголовку
         * Для объявлений типа DEMAND подбираются SUPPLY и наоборот
         */
        val filterResponse = if (adRequest == null) {
            DbAdsResponse(
                result = null,
                isSuccess = false,
                errors = adResponse.errors
            )
        } else {
            when(adRequest.adType) {
                MkplDealSide.DEMAND -> adRepo.searchAd(DbAdFilterRequest(titleFilter = adRequest.title, dealSide = MkplDealSide.SUPPLY))
                MkplDealSide.SUPPLY -> adRepo.searchAd(DbAdFilterRequest(titleFilter = adRequest.title, dealSide = MkplDealSide.DEMAND))
                else -> DbAdsResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(
                        MkplError(
                            field = "adType",
                            message = "Type of ad must not be empty"
                        )
                    )
                )
            }
        }

        val resultAds = filterResponse.result
        if (filterResponse.isSuccess && resultAds != null) {
            adsResponse = resultAds.toMutableList()
        } else {
            state = MkplState.FAILING
            errors.addAll(filterResponse.errors)
        }
    }
}
