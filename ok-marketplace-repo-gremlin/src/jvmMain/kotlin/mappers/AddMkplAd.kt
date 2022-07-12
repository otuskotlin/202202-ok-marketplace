package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBolt
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductNone

fun GraphTraversal<Vertex, Vertex>.addMkplAd(ad: MkplAd): GraphTraversal<Vertex, Vertex>? =
    addV(ad.label())
        .property(VertexProperty.Cardinality.single,"id", ad.id.asString())
        .property(VertexProperty.Cardinality.single,"title", ad.title)
        .property(VertexProperty.Cardinality.single,"description", ad.description)
        .property(VertexProperty.Cardinality.single,"lock", ad.lock.asString())
        .property(VertexProperty.Cardinality.single,"ownerId", ad.ownerId.asString()) // здесь можно сделать ссылку на объект владельца
        .property(VertexProperty.Cardinality.single,"adType", ad.adType.name)
        .property(VertexProperty.Cardinality.single,"visibility", ad.visibility.name)
        .apply {
            when (val product = ad.product) {
                is MkplAdProductBolt -> addMkplAdProductBold(product)
                MkplAdProductNone -> addMkplAdProductNone()
            }
        }
