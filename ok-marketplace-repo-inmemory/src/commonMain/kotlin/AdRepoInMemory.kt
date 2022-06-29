package ru.otus.otuskotlin.marketplace.backend.repository.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model.AdEntity
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AdRepoInMemory constructor(
    private val initObjects: List<MkplAd> = emptyList(),
    ttl: Duration = 2.minutes
): IAdRepository {
    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */
    private val cache =  Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, AdEntity>()

    init {
        runBlocking { initObjects.forEach {
            save(it)
        } }
    }

    private fun save(ad: MkplAd): DbAdResponse {
        val entity = AdEntity(ad)
        if (entity.id == null) {
            return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    MkplError(
                        field = "id",
                        message = "Id must not be null or empty",
                    )
                )
            )
        }
        cache.put(entity.id, entity)
        return DbAdResponse(
            result = entity.toInternal(),
            isSuccess = true,
        )
    }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse =
        save(rq.ad.copy(id = MkplAdId(uuid4().toString())))

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse =
        getOrRemoveById(rq.id)

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val key = rq.ad.id.takeIf { it != MkplAdId.NONE }?.asString()
            ?: return DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    MkplError(
                        field = "id",
                        message = "Id must not be null or blank")
                )
            )

        return if (cache.asMap().containsKey(key)) {
            save(rq.ad)
        } else {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    MkplError(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
        }
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse  =
        getOrRemoveById(rq.id, true)

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != MkplUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                }?: true
            }
            .filter { entry ->
                rq.dealSide.takeIf { it != MkplDealSide.NONE }?.let {
                    it.name == entry.value.adType
                }?: true
            }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it)?: false
                }?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbAdsResponse(
            result = result,
            isSuccess = true
        )
    }

    private fun getOrRemoveById(id: MkplAdId, remove: Boolean = false): DbAdResponse =
        if (id != MkplAdId.NONE) {
        cache.get(id.asString())?.let {
            if (remove)
                cache.invalidate(it.id!!)
            DbAdResponse(
                result = it.toInternal(),
                isSuccess = true,
            )
        }?: DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    field = "id",
                    message = "Not Found",
                )
            )
        )
    } else {
        DbAdResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    field = "id",
                    message = "Id must not be null or empty",
                )
            )
        )
    }

}
