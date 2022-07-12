package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdSearchTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class AdRepoInMemoryUpdateTest: RepoAdUpdateTest() {
    override val repo: IAdRepository by lazy {
        AdRepoGremlin(
            hosts = "localhost",
            port = ArcadeDbContainer.container.getMappedPort(8182),
            initObjects = RepoAdSearchTest.initObjects
        )
    }
}
