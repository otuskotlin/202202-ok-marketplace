package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures
import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdCassandraDTO
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdDealSide
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdPermissionClient
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdVisibility
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdCreateTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdDeleteTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdReadTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdSearchTest
import ru.otus.otuskotlin.marketplace.backend.repo.test.RepoAdUpdateTest
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdPermissionClient
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import java.net.InetSocketAddress

class RepoAdCassandraCreateTest : RepoAdCreateTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_create")
}

class RepoAdCassandraDeleteTest : RepoAdDeleteTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_delete")
}

class RepoAdCassandraReadTest : RepoAdReadTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_read")
}

class RepoAdCassandraSearchTest : RepoAdSearchTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_search")
}

class RepoAdCassandraUpdateTest : RepoAdUpdateTest() {
    override val repo: IAdRepository = TestCompanion.repository(initObjects, "ks_update")
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy { TestCasandraContainer().apply { start() } }

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(AdVisibility::class.java))
            register(EnumNameCodec(AdDealSide::class.java))
            register(EnumNameCodec(AdPermissionClient::class.java))
        }
    }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(AdCassandraDTO.table(keyspace, AdCassandraDTO.TABLE_NAME))
        session.execute(AdCassandraDTO.titleIndex(keyspace, AdCassandraDTO.TABLE_NAME))
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoint(InetSocketAddress(container.host, container.getMappedPort(CassandraContainer.CQL_PORT)))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(container.username, container.password)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    fun repository(initObjects: List<MkplAd>, keyspace: String): RepoAdCassandra {
        createSchema(keyspace)
        val dao = mapper.adDao(keyspace, AdCassandraDTO.TABLE_NAME)
        CompletableFutures
            .allDone(initObjects.map { dao.create(AdCassandraDTO(it)) })
            .toCompletableFuture()
            .get()

        return RepoAdCassandra(dao)
    }
}
