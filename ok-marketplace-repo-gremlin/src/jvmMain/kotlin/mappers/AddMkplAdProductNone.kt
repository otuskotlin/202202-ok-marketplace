package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import ru.otus.otuskotlin.marketplace.common.models.product.IMkplAdProduct

fun GraphTraversal<Vertex, Vertex>.addMkplAdProductNone(): GraphTraversal<Vertex, Vertex> =
    this
