package ru.otus.otuskotlin.marketplace.backend.repo.cassandra

import kotlinx.coroutines.future.await
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model.AdCassandraDTO
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponse
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import java.util.*

class RepoAdCassandra(
    private val dao: AdCassandraDAO,
    private val timeoutMillis: Long = 30_000
) : IAdRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private suspend fun doDbAction(name: String, action: suspend () -> DbAdResponse): DbAdResponse =
        try {
            action()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            DbAdResponse.error(e.asMkplError())
        }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val new = rq.ad.copy(id = MkplAdId(UUID.randomUUID().toString()))
        return doDbAction("create") {
            withTimeout(timeoutMillis) { dao.create(AdCassandraDTO(new)).await() }
            DbAdResponse.success(new)
        }
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        return if (rq.id == MkplAdId.NONE)
            ID_IS_EMPTY
        else doDbAction("read") {
            val found = withTimeout(timeoutMillis) { dao.read(rq.id.asString()).await() }
            if (found != null) DbAdResponse.success(found.toAdModel())
            else ID_NOT_FOUND
        }
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        return if (rq.ad.id == MkplAdId.NONE)
            ID_IS_EMPTY
        else doDbAction("update") {
            val updated = withTimeout(timeoutMillis) {
                dao.update(AdCassandraDTO(rq.ad)).await()
                dao.read(rq.ad.id.asString()).await()
            }
            if (updated != null) DbAdResponse.success(updated.toAdModel())
            else DbAdResponse.error(MkplError(field = "id", message = "Not Found"))
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return if (rq.id == MkplAdId.NONE)
            ID_IS_EMPTY
        else doDbAction("delete") {
            val deleted = withTimeout(timeoutMillis) {
                dao.read(rq.id.asString()).await()
                    ?.also { dao.delete(it).await() }
            }
            if (deleted != null) DbAdResponse.success(deleted.toAdModel())
            else ID_NOT_FOUND
        }
    }


    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        return try {
            val found = withTimeout(timeoutMillis) { dao.search(rq) }.await()
            DbAdsResponse.success(found.map { it.toAdModel() })
        } catch (e: Exception) {
            log.error("Failed to search", e)
            DbAdsResponse.error(e.asMkplError())
        }
    }

    companion object {
        private val ID_IS_EMPTY = DbAdResponse.error(MkplError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND = DbAdResponse.error(MkplError(field = "id", message = "Not Found"))
    }
}