package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId

class AdRepoGremlinUpdateTest: RepoAdUpdateTest() {
    override val repo: AdRepoGremlin by lazy {
        AdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
            randomUuid = { newLock.asString() }
        )
    }
    override val updateId: MkplAdId = repo.initializedObjects.first().id
    override val updateObj: MkplAd = super.updateObj.copy(id = updateId)
}
