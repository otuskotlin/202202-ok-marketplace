package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoAdDeleteTest {
    abstract val repo: IAdRepository
    protected open val successId = MkplAdId(deleteSuccessStub.id.asString())


    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.deleteAd(DbAdIdRequest(id = successId, lock = deleteSuccessStub.lock)) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(deleteSuccessStub.copy(id = successId), result.result)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.readAd(DbAdIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(MkplError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitAds("delete") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("delete")
        )
        private val deleteSuccessStub = initObjects.first()
        val notFoundId = MkplAdId("ad-repo-delete-notFound")
    }
}
