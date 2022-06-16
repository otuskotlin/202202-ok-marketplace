package ru.otus.otuskotlin.marketplace.common.repo

interface IAdRepository {
    suspend fun createAd(rq: DbAdRequest): DbAdResponse
    suspend fun readAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun updateAd(rq: DbAdRequest): DbAdResponse
    suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse
    suspend fun searchOffers(rq: DbAdIdRequest): DbAdsResponse
}
