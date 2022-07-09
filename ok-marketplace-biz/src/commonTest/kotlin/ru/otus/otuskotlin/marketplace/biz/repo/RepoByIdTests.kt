package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Test
import kotlin.test.assertEquals

private val initAd = MkplAd(
    id = MkplAdId("123"),
    title = "abc",
    description = "abc",
    adType = MkplDealSide.DEMAND,
    visibility = MkplVisibility.VISIBLE_PUBLIC,
)
private val repo: IAdRepository
    get() = AdRepoInMemory(initObjects = listOf(initAd))


@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(processor: MkplAdProcessor, command: MkplCommand) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        adRepo = repo,
        adRequest = MkplAd(
            id = MkplAdId("12345"),
            title = "xyz",
            description = "xyz",
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
        ),
    )
    processor.exec(ctx)
    assertEquals(MkplState.FAILING, ctx.state)
    assertEquals(MkplAd(), ctx.adResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
