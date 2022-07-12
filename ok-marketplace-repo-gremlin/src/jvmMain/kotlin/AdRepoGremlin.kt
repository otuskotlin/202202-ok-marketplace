package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.structure.T
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.exceptions.DbDuplicatedElementsException
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.exceptions.WrongIdTypeException
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.addMkplAd
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.label
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.toMkplAd
import ru.otus.otuskotlin.marketplace.common.helpers.errorAdministration
import ru.otus.otuskotlin.marketplace.common.helpers.errorConcurrency
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*


actual class AdRepoGremlin actual constructor(
    private val hosts: String,
    private val port: Int,
    private val enableSsl: Boolean,
    initObjects: List<MkplAd>,
    val randomUuid: () -> String,
) : IAdRepository {

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(ad: MkplAd) {
        if (ad.id == MkplAdId.NONE) {
            return
        }
        g.addV(ad.label()).addMkplAd(ad)?.iterate()
    }

    actual override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val key = randomUuid()
        val ad = rq.ad.copy(id = MkplAdId(key), lock = MkplAdLock(randomUuid()))
        val id = g.addV(ad.label()).addMkplAd(ad)
            ?.next()
            ?.id()
            .let {
                when (it) {
                    is String -> it
                    else -> return DbAdResponse(
                        result = null, isSuccess = false,
                        errors = listOf(
                            errorAdministration(
                                violationCode = "badDbResponse",
                                description = "Unexpected result got. Please, contact administrator",
                                exception = WrongIdTypeException(
                                    "createAd for ${this@AdRepoGremlin::class} " +
                                            "returned id = '$it' that is not addmitted by the application"
                                )
                            )
                        )
                    )
                }
            }

        return DbAdResponse(
            result = ad.copy(id = MkplAdId(id)),
            isSuccess = true,
        )
    }

    actual override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = g.V(key).has(T.id, key).elementMap<Any>().toList()
        when (dbRes.size) {
            0 -> return resultErrorNotFound
            1 -> return DbAdResponse(
                result = dbRes.first().toMkplAd(),
                isSuccess = true,
            )
            else -> return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    errorAdministration(
                        violationCode = "duplicateObjects",
                        description = "Database consistency failure",
                        exception = DbDuplicatedElementsException("Db contains multiple elements for id = '$key'")
                    )
                )
            )
        }
    }

    actual override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val key = rq.ad.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.ad.lock.takeIf { it != MkplAdLock.NONE }?.asString()
        val newAd = rq.ad.copy(lock = MkplAdLock(randomUuid()))
        g.V(key).addMkplAd(newAd)
//        mutex.withLock {
//            val local = cache.get(key)
//            when {
//                local == null -> return resultErrorNotFound
//                local.lock == null || local.lock == oldLock -> cache.put(key, entity)
//                else -> return resultErrorConcurrent
//            }
//        }
        return DbAdResponse(
            result = newAd,
            isSuccess = true,
        )
    }

    actual override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
//        mutex.withLock {
//            val local = cache.get(key)
//            if (local?.lock == null || local.lock == rq.lock.asString()) {
//                cache.invalidate(key)
//                return DbAdResponse(
//                    result = null,
//                    isSuccess = true,
//                    errors = emptyList()
//                )
//            } else {
        return resultErrorConcurrent
//            }
//        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    actual override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
//        val result = cache.asMap().asSequence()
//            .filter { entry ->
//                rq.ownerId.takeIf { it != MkplUserId.NONE }?.let {
//                    it.asString() == entry.value.ownerId
//                } ?: true
//            }
//            .filter { entry ->
//                rq.dealSide.takeIf { it != MkplDealSide.NONE }?.let {
//                    it.name == entry.value.adType
//                } ?: true
//            }
//            .filter { entry ->
//                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
//                    entry.value.title?.contains(it) ?: false
//                } ?: true
//            }
//            .map { it.value.toInternal() }
//            .toList()
        return DbAdsResponse(
//            result = result,
            result = null,
            isSuccess = true
        )
    }

    companion object {
        val resultErrorEmptyId = DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        val resultErrorConcurrent = DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                errorConcurrency(
                    violationCode = "changed",
                    description = "Object has changed during request handling"
                ),
            )
        )
        val resultErrorNotFound = DbAdResponse(
            isSuccess = false,
            result = null,
            errors = listOf(
                MkplError(
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
