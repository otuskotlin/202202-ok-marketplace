package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductNone

fun GraphTraversal<Vertex, Vertex>.addMkplAdProductNone(): GraphTraversal<Vertex, Vertex> = this
    .property(VertexProperty.Cardinality.single, "product.type", MkplAdProductNone::class.simpleName)
