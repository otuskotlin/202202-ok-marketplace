import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoInMemory
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository

class AdRepoInMemoryUpdateTest: RepoAdUpdateTest() {
    override val repo: IAdRepository = AdRepoInMemory(
        initObjects = initObjects
    )
}
