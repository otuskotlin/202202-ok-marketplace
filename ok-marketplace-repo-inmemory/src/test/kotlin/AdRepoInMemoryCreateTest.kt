import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdCreateTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class AdRepoInMemoryCreateTest: RepoAdCreateTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )
}
