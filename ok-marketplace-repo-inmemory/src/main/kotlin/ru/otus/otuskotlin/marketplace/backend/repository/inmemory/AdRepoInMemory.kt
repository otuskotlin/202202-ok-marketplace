package ru.otus.otuskotlin.marketplace.backend.repository.inmemory

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model.AdEntity
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import java.time.Duration
import java.util.UUID

class AdRepoInMemory(
    private val initObjects: List<MkplAd> = emptyList(),
    private val ttl: Duration = Duration.ofMinutes(2),
): IAdRepository {
    private val cache: Cache<String, AdEntity> = let {
        val cacheManager: CacheManager = CacheManagerBuilder
            .newCacheManagerBuilder()
            .build(true)

        cacheManager.createCache(
            "mkpl-ad-cache",
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                    String::class.java,
                    AdEntity::class.java,
                    ResourcePoolsBuilder.heap(100)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                .build()
        )
    }

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
        save(rq.ad.copy(id = MkplAdId(UUID.randomUUID().toString())))

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

        return if (cache.containsKey(key)) {
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

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        val result = cache.asFlow()
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

    override suspend fun searchOffers(rq: DbAdIdRequest): DbAdsResponse {
        val entityResponse = getOrRemoveById(rq.id)
        if (entityResponse.isSuccess) {
            with(entityResponse.result!!) {
                if (adType == MkplDealSide.NONE) {
                    return DbAdsResponse(
                        result = null,
                        isSuccess = false,
                        errors = listOf(
                            MkplError(
                                field = "adType",
                                message = "Type of ad must not be empty"
                            )
                        ),
                    )
                }
                val result = cache.asFlow()
                    .filter {
                        when(adType) {
                            MkplDealSide.DEMAND -> it.value.adType == MkplDealSide.SUPPLY.name
                            MkplDealSide.SUPPLY -> it.value.adType == MkplDealSide.DEMAND.name
                            else -> false
                        }
                    }
                    .filter {
                        it.value.title?.contains(title)?: false
                    }
                    .map { it.value.toInternal() }
                    .toList()
                return DbAdsResponse(
                    result = result,
                    isSuccess = true
                )
            }
        } else return DbAdsResponse(
            result = null,
            isSuccess = false,
            errors = entityResponse.errors
        )
    }

    private fun getOrRemoveById(id: MkplRequestId, remove: Boolean = false): DbAdResponse =
        if (id != MkplRequestId.NONE) {
        cache.get(id.asString())?.let {
            if (remove)
                cache.remove(it.id)
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
