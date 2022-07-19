package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_AD_TYPE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_DESCRIPTION
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_LOCK
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_OWNER_ID
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_TITLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_VISIBILITY
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBolt
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductNone

fun GraphTraversal<Vertex, Vertex>.addMkplAd(ad: MkplAd): GraphTraversal<Vertex, Vertex>? =
    this
        .property(VertexProperty.Cardinality.single,FIELD_TITLE, ad.title)
        .property(VertexProperty.Cardinality.single, FIELD_DESCRIPTION, ad.description)
        .property(VertexProperty.Cardinality.single, FIELD_LOCK, ad.lock.asString())
        .property(VertexProperty.Cardinality.single, FIELD_OWNER_ID, ad.ownerId.asString()) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single, FIELD_AD_TYPE, ad.adType.name)
        .property(VertexProperty.Cardinality.single, FIELD_VISIBILITY, ad.visibility.name)
        .apply {
            when (val product = ad.product) {
                is MkplAdProductBolt -> addMkplAdProductBold(product)
                MkplAdProductNone -> addMkplAdProductNone()
            }
        }
