package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoAdSearchTest {
    abstract val repo: IAdRepository
    protected open val initAds: List<MkplAd> = initObjects

    @Test
    fun searchOwner() {
        val result = runBlocking { repo.searchAd(DbAdFilterRequest(ownerId = searchOwnerId)) }
        assertEquals(true, result.isSuccess)
        val expected = initAds.filter { it.ownerId == searchOwnerId }.sortedBy { it.id.asString() }
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchDealSide() {
        val result = runBlocking { repo.searchAd(DbAdFilterRequest(dealSide = MkplDealSide.SUPPLY)) }
        assertEquals(true, result.isSuccess)
        val expected = initAds.filter { it.adType == MkplDealSide.SUPPLY }.sortedBy { it.id.asString() }
        assertEquals(expected, result.result?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("search") {

        val searchOwnerId = MkplUserId("owner-124")
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad3", adType = MkplDealSide.SUPPLY),
            createInitTestModel("ad4", ownerId = searchOwnerId),
            createInitTestModel("ad5", adType = MkplDealSide.SUPPLY),
        )
    }
}
