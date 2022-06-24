import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdOffersTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class AdRepoInMemoryOffersTest: RepoAdOffersTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )
}
