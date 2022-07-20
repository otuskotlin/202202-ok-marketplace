package ru.otus.otuskotlin.marketplace.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class RepoAdReadTest {
    abstract val repo: IAdRepository
    protected open val successId = Companion.successId

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.readAd(DbAdIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub.copy(successId), result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.readAd(DbAdIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(MkplError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitAds("read") {
        override val initObjects: List<MkplAd> = listOf(
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()

        private val successId = MkplAdId(readSuccessStub.id.asString())
        val notFoundId = MkplAdId("ad-repo-read-notFound")

    }
}
