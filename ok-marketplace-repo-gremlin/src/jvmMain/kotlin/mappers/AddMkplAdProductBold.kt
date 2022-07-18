package ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLD_HEAD_STYLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_DIAMETER_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_DIAMETER_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_LENGTH_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_LENGTH_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_CONF
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_LEN
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_BOLT_THREAD_PITCH_UNIT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_PRODUCT_TYPE
import ru.otus.otuskotlin.marketplace.common.models.product.MkplAdProductBolt

fun GraphTraversal<Vertex, Vertex>.addMkplAdProductBold(product: MkplAdProductBolt): GraphTraversal<Vertex, Vertex> =
    property("product.diameter", product.diameter)
        .property(VertexProperty.Cardinality.single,FIELD_PRODUCT_TYPE, MkplAdProductBolt::class.simpleName)
        .property(VertexProperty.Cardinality.single, FIELD_BOLD_HEAD_STYLE, product.headStyle.name)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_LENGTH_LEN, product.length.len)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_LENGTH_UNIT, product.length.unit)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_DIAMETER_LEN, product.length.len)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_DIAMETER_UNIT, product.length.unit)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_THREAD_PITCH_LEN, product.thread.pitch.len)
        .property(VertexProperty.Cardinality.single,FIELD_BOLT_THREAD_PITCH_UNIT, product.thread.pitch.unit.name)
        .property(VertexProperty.Cardinality.single, FIELD_BOLT_THREAD_PITCH_CONF, product.thread.pitchConf.name)
