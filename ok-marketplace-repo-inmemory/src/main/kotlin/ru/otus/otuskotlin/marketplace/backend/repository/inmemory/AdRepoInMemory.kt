package ru.otus.otuskotlin.marketplace.backend.repository.inmemory

import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.model.AdEntity
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.repo.*
import java.time.Duration

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

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchOffers(rq: DbAdIdRequest): DbAdsResponse {
        TODO("Not yet implemented")
    }
}
