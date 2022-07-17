package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.junit.Ignore
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdSearchTest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

@Ignore
class AdRepoInMemorySearchTest: RepoAdSearchTest() {
    override val repo: IAdRepository by lazy {
        AdRepoGremlin(
            hosts = "localhost",
            port = ArcadeDbContainer.container.getMappedPort(2424),
            initObjects = initObjects
        )
    }
}
