package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoAdUpdateTest {
    abstract val repo: IAdRepository
    protected open val updateId = initObjects.first().id
    protected open val newLock = MkplAdLock("20000000-0000-0000-0000-000000000001")
    protected open val updateObj = MkplAd(
        id = updateId,
        title = "update object",
        description = "update object description",
        ownerId = MkplUserId("owner-123"),
        visibility = MkplVisibility.VISIBLE_TO_GROUP,
        adType = MkplDealSide.SUPPLY,
        lock = initObjects.first().lock,
    )


    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.updateAd(DbAdRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.id, result.result?.id)
        assertEquals(updateObj.title, result.result?.title)
        assertEquals(updateObj.description, result.result?.description)
        assertEquals(updateObj.adType, result.result?.adType)
        assertEquals(newLock, result.result?.lock)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.updateAd(DbAdRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(MkplError(field = "id", message = "Not Found")), result.errors)
    }

    companion object : BaseInitAds("update") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("update")
        )
        private val updateIdNotFound = MkplAdId("ad-repo-update-not-found")

        private val updateObjNotFound = MkplAd(
            id = updateIdNotFound,
            title = "update object not found",
            description = "update object not found description",
            ownerId = MkplUserId("owner-123"),
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            adType = MkplDealSide.SUPPLY,
        )
    }
}
