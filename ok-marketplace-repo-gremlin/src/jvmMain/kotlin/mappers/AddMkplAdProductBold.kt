package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBolt

fun GraphTraversal<Vertex, Vertex>.addMkplAdProductBold(product: MkplAdProductBolt): GraphTraversal<Vertex, Vertex> =
    property("product.diameter", product.diameter)
        .property(VertexProperty.Cardinality.single,"product.type", MkplAdProductBolt::class.simpleName)
        .property(VertexProperty.Cardinality.single,"product.headStyle", product.headStyle.name)
        .property(VertexProperty.Cardinality.single,"product.lengh", product.length)
        .property(VertexProperty.Cardinality.single,"product.thread.pitch.len", product.thread.pitch.len)
        .property(VertexProperty.Cardinality.single,"product.thread.pitch.unit", product.thread.pitch.unit.name)
        .property(VertexProperty.Cardinality.single,"product.thread.pitchConf", product.thread.pitchConf.name)
