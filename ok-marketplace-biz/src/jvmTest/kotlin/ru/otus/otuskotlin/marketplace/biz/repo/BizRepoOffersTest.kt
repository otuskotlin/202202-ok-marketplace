package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.helpers.principalUser
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoOffersTest {
    private val principal = principalUser()
    private val command = MkplCommand.OFFERS
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        ownerId = principal.id,
    )
    private val noneTypeAd = MkplAd(
        id = MkplAdId("213"),
        title = "abc",
        description = "abc",
        adType = MkplDealSide.NONE,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        ownerId = principal.id,
    )
    private val offerAd = MkplAd(
        id = MkplAdId("321"),
        title = "abcd",
        description = "xyz",
        adType = MkplDealSide.SUPPLY,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        ownerId = principal.id,
    )
    private val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd, offerAd, noneTypeAd)) }
    private val settings by lazy {
        MkplSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { MkplAdProcessor(settings) }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoOffersSuccessTest() = runTest {
        val ctx = MkplContext(
            command = command,
            principal = principal,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = MkplAdId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(1, ctx.adsResponse.size)
        assertEquals(MkplDealSide.SUPPLY, ctx.adsResponse.first().adType)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoOffersIllegalTypeTest() = runTest {
        val ctx = MkplContext(
            command = command,
            principal = principal,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = MkplAdId("213"),
            ),
        )
        processor.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("adType", ctx.errors.first().field)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repoOffersNotFoundTest() = repoNotFoundTest(processor, command)
}
