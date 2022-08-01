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

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {

    private val command = MkplCommand.UPDATE
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"
    private val uuidBad = "10000000-0000-0000-0000-000000000003"
    private val initAd = MkplAd(
        id = MkplAdId("123"),
        title = "abc",
        description = "abc",
        adType = MkplDealSide.DEMAND,
        visibility = MkplVisibility.VISIBLE_PUBLIC,
        lock = MkplAdLock(uuidOld),
        ownerId = principalUser().id,
    )
    private val repo by lazy { AdRepoInMemory(initObjects = listOf(initAd), randomUuid = { uuidNew }) }
    private val settings by lazy {
        MkplSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { MkplAdProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = MkplAd(
            id = MkplAdId("123"),
            title = "xyz",
            description = "xyz",
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = MkplAdLock(uuidOld),
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            principal = principalUser(),
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkplState.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.adResponse.id)
        assertEquals(adToUpdate.title, ctx.adResponse.title)
        assertEquals(adToUpdate.description, ctx.adResponse.description)
        assertEquals(adToUpdate.adType, ctx.adResponse.adType)
        assertEquals(adToUpdate.visibility, ctx.adResponse.visibility)
        assertEquals(uuidNew, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoUpdateConcurrentTest() = runTest {
        val adToUpdate = MkplAd(
            id = MkplAdId("123"),
            title = "xyz",
            description = "xyz",
            adType = MkplDealSide.DEMAND,
            visibility = MkplVisibility.VISIBLE_TO_GROUP,
            lock = MkplAdLock(uuidBad)
        )
        val ctx = MkplContext(
            command = command,
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            principal = principalUser(),
            adRequest = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(MkplState.FAILING, ctx.state)
        assertEquals(initAd.id, ctx.adResponse.id)
        assertEquals(initAd.title, ctx.adResponse.title)
        assertEquals(initAd.description, ctx.adResponse.description)
        assertEquals(initAd.adType, ctx.adResponse.adType)
        assertEquals(initAd.visibility, ctx.adResponse.visibility)
        assertEquals(uuidOld, ctx.adResponse.lock.asString())
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(processor, command)
}
