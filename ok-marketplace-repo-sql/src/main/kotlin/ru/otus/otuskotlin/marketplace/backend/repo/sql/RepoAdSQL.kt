package ru.otus.otuskotlin.marketplace.backend.repo.sql

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.repo.DbAdFilterRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdIdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdRequest
import ru.otus.otuskotlin.marketplace.common.repo.DbAdResponse
import ru.otus.otuskotlin.marketplace.common.repo.DbAdsResponse
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import java.sql.SQLException

class RepoAdSQL(
    url: String = "jdbc:postgresql://localhost:5432/marketplacedevdb",
    user: String = "postgres",
    password: String = "marketplace-pass",
    schema: String = "marketplace",
    initObjects: Collection<MkplAd> = emptyList()
) : IAdRepository {
    private val db by lazy { SqlConnector(url, user, password, schema).connect(AdsTable, UsersTable) }

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(item: MkplAd): DbAdResponse {
        return safeTransaction({
            val realOwnerId = UsersTable.insertIgnore {
                if (item.ownerId != MkplUserId.NONE) {
                    it[id] = item.ownerId.asString()
                    it[name] = item.ownerId.asString()
                }
            } get UsersTable.id

            val res = AdsTable.insert {
                if (item.id != MkplAdId.NONE) {
                    it[id] = item.id.asString()
                }
                it[title] = item.title
                it[description] = item.description
                it[ownerId] = realOwnerId
                it[visibility] = item.visibility
                it[adType] = item.adType
            }

            DbAdResponse(AdsTable.from(res), true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(MkplError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        return save(rq.ad)
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        return safeTransaction({
            val result = (AdsTable innerJoin UsersTable).select { AdsTable.id.eq(rq.id.asString()) }.single()

            DbAdResponse(AdsTable.from(result), true)
        }, {
            val err = when (this) {
                is NoSuchElementException -> MkplError(field = "id", message = "Not Found")
                is IllegalArgumentException -> MkplError(message = "More than one element with the same id")
                else -> MkplError(message = localizedMessage)
            }
            DbAdResponse(result = null, isSuccess = false, errors = listOf(err))
        })
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val ad = rq.ad
        return safeTransaction({
            UsersTable.insertIgnore {
                if (ad.ownerId != MkplUserId.NONE) {
                    it[id] = ad.ownerId.asString()
                }
                it[name] = ad.ownerId.asString().toString()
            }
            UsersTable.update({ UsersTable.id.eq(ad.ownerId.asString()) }) {
                it[name] = ad.ownerId.asString().toString()
            }

            AdsTable.update({ AdsTable.id.eq(ad.id.asString()) }) {
                it[title] = ad.title
                it[description] = ad.description
                it[ownerId] = ad.ownerId.asString()
                it[visibility] = ad.visibility
                it[adType] = ad.adType
            }
            val result = AdsTable.select { AdsTable.id.eq(ad.id.asString()) }.single()

            DbAdResponse(result = AdsTable.from(result), isSuccess = true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(MkplError(field = "id", message = "Not Found"))
            )
        })
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return safeTransaction({
            val result = AdsTable.select { AdsTable.id.eq(rq.id.asString()) }.single()
            AdsTable.deleteWhere { AdsTable.id eq rq.id.asString() }

            DbAdResponse(result = AdsTable.from(result), isSuccess = true)
        }, {
            DbAdResponse(
                result = null,
                isSuccess = false,
                errors = listOf(MkplError(field = "id", message = "Not Found"))
            )
        })
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        return safeTransaction({
            // Select only if options are provided
            val results = (AdsTable innerJoin UsersTable).select {
                (if (rq.ownerId == MkplUserId.NONE) Op.TRUE else AdsTable.ownerId eq rq.ownerId.asString()) and
                    (
                        if (rq.titleFilter.isBlank()) Op.TRUE else (AdsTable.title like "%${rq.titleFilter}%") or
                            (AdsTable.description like "%${rq.titleFilter}%")
                        ) and
                    (if (rq.dealSide == MkplDealSide.NONE) Op.TRUE else AdsTable.adType eq rq.dealSide)
            }

            DbAdsResponse(result = results.map { AdsTable.from(it) }, isSuccess = true)
        }, {
            DbAdsResponse(result = emptyList(), isSuccess = false, listOf(MkplError(message = localizedMessage)))
        })
    }

    /**
     * Transaction wrapper to safely handle caught exception and throw all sql-like exceptions. Also remove lot's of duplication code
     */
    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }
}
