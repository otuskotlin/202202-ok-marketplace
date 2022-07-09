package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val processor = MkplAdProcessor()
    private val command = MkplCommand.DELETE
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
    )
    private val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd)) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = MkplAd(
            id = MkplAdId("123"),
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRepo = repo,
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(processor, command)
}
