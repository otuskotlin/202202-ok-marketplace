package ru.otus.otuskotlin.marketplace.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.biz.helpers.principalUser
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplAdLock
import ru.otus.otuskotlin.marketplace.common.models.MkplAdPermissionClient
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplSettings
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.stubs.Bolt
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStubBolts
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BizCrudAuthTest {
    private val readStub = Bolt.getModel(null)
    private val settings = MkplSettings(
            repoTest = AdRepoInMemory(listOf(readStub))
        )

    private val processor = MkplAdProcessor(settings)

    @Test
    fun createSuccessTest() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.CREATE,
            principal = principalUser(),
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = MkplAdId("123"),
                title = "abc",
                description = "abc",
                adType = MkplDealSide.DEMAND,
                visibility = MkplVisibility.VISIBLE_PUBLIC,
                lock = MkplAdLock(MkplAdStubBolts.uuidOld),
            ),
        )

        processor.exec(ctx)

        assertNotEquals(MkplState.FAILING, ctx.state)

        with(ctx.adResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, MkplAdPermissionClient.READ)
            assertContains(permissionsClient, MkplAdPermissionClient.UPDATE)
            assertContains(permissionsClient, MkplAdPermissionClient.DELETE)
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val ctx = MkplContext(
            command = MkplCommand.READ,
            principal = principalUser(readStub.ownerId),
            state = MkplState.NONE,
            workMode = MkplWorkMode.TEST,
            adRequest = MkplAd(
                id = readStub.id,
            ),
        )

        processor.exec(ctx)

        assertNotEquals(MkplState.FAILING, ctx.state)

        with(ctx.adResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, MkplAdPermissionClient.READ)
            assertContains(permissionsClient, MkplAdPermissionClient.UPDATE)
            assertContains(permissionsClient, MkplAdPermissionClient.DELETE)
        }
    }

}
