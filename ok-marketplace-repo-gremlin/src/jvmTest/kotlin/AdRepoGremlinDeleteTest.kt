package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdDeleteTest
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId

class AdRepoGremlinDeleteTest: RepoAdDeleteTest() {
    override val repo: AdRepoGremlin by lazy {

        AdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            initObjects = initObjects,
        )
    }
    override val successId: MkplAdId = repo.initializedObjects.first().id
}
