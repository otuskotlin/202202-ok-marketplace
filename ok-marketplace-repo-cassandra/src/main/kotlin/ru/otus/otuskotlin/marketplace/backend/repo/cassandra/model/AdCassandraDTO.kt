package ru.otus.otuskotlin.marketplace.backend.repo.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.MkplAdId
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId

@Entity
data class AdCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey
    var id: String? = null,
    @field:CqlName(COLUMN_TITLE)
    var title: String? = null,
    @field:CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_VISIBILITY)
    var visibility: AdVisibility? = null,
    @field:CqlName(COLUMN_AD_TYPE)

    // Нельзя использовать в моделях хранения внутренние модели.
    // При изменении внутренних моделей, БД автоматически не изменится,
    // а потому будет Runtime ошибка, которая вылезет только на продуктовом стенде
    var adType: AdDealSide? = null,
    @field:CqlName(COLUMN_PERMISSIONS)
    var permissions: Set<AdPermissionClient>? = null
) {
    constructor(adModel: MkplAd) : this(
        ownerId = adModel.ownerId.takeIf { it != MkplUserId.NONE }?.asString(),
        id = adModel.id.takeIf { it != MkplAdId.NONE }?.asString(),
        title = adModel.title.takeIf { it.isNotBlank() },
        description = adModel.description.takeIf { it.isNotBlank() },
        visibility = adModel.visibility.toTransport(),
        adType = adModel.adType.toTransport(),
        permissions = adModel.permissionsClient.takeIf { it.isNotEmpty() }?.mapTo(mutableSetOf()) { it.toTransport() }
    )

    fun toAdModel(): MkplAd =
        MkplAd(
            ownerId = ownerId?.let { MkplUserId(it) } ?: MkplUserId.NONE,
            id = id?.let { MkplAdId(it) } ?: MkplAdId.NONE,
            title = title ?: "",
            description = description ?: "",
            visibility = visibility.fromTransport(),
            adType = adType.fromTransport(),
            permissionsClient = permissions.orEmpty().mapTo(mutableSetOf()) { it.fromTransport() }
        )

    companion object {
        const val TABLE_NAME = "ads"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_OWNER_ID = "\"owner_id_my\""
        const val COLUMN_VISIBILITY = "visibility"
        const val COLUMN_AD_TYPE = "deal_side"
        const val COLUMN_PERMISSIONS = "permissions"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_TITLE, DataTypes.TEXT)
                .withColumn(COLUMN_DESCRIPTION, DataTypes.TEXT)
                .withColumn(COLUMN_OWNER_ID, DataTypes.TEXT)
                .withColumn(COLUMN_VISIBILITY, DataTypes.TEXT)
                .withColumn(COLUMN_AD_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_PERMISSIONS, DataTypes.setOf(DataTypes.TEXT))
                .build()

        fun titleIndex(keyspace: String, tableName: String, locale: String = "en") =
            SchemaBuilder
                .createIndex()
                .ifNotExists()
                .usingSASI()
                .onTable(keyspace, tableName)
                .andColumn(COLUMN_TITLE)
                .withSASIOptions(mapOf("mode" to "CONTAINS", "tokenization_locale" to locale))
                .build()
    }
}