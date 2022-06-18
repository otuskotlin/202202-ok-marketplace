package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAdOffersTest {
    abstract val repo: IAdRepository

    @Test
    fun searchByTitle() {
        val result = runBlocking { repo.searchOffers(DbAdIdRequest(searchId)) }
        assertEquals(true, result.isSuccess)
        assertEquals(2, result.result?.size)
        result.result?.forEach {
            assertEquals(MkplDealSide.SUPPLY, it.adType)
            assert(it.title.contains(searchSuf))
        }
    }

    companion object : BaseInitAds("search") {
        val searchSuf = "ad1"
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel(suf = searchSuf, adType = MkplDealSide.DEMAND),
            createInitTestModel(suf = searchSuf, adType = MkplDealSide.SUPPLY),
            createInitTestModel(suf = searchSuf, adType = MkplDealSide.SUPPLY)
        )
        val searchId = initObjects.first().id

    }
}
