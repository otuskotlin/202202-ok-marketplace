package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.structure.T
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.junit.Test
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as bs

//@Ignore
class TmpTest {
    @Test
    fun x() {
        val host = ArcadeDbContainer.container.host
        val port = ArcadeDbContainer.container.getMappedPort(8182)
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(port)
//            enableSsl(enableSsl)
        }.create()
        val g = AnonymousTraversalSource
            .traversal()
            .withRemote(DriverRemoteConnection.using(cluster))
        val id = g.inject("zzz")
            .addV("Test")
            .property("ID", "val-1")
            .property("lock", "111")
            .next()
            .id()
        println("ID: $id")

        val m = g
            .V(id)
            .`as`("a")
            .choose<Any>(
                bs.select<Any, Any>("a")
                    .values<String>("lock")
                    .`is`("1121"),
                bs.select<Any, Any>("a").inject("kjh"),
                bs.select<Any,Any>("a")
            ).elementMap<Any>().toList()
        println("XXX: $m")
        val n = g
            .V(id)
            .`as`("a")
            .choose<Any>(
                bs.select<Vertex, Any>("a")
                    .values<String>("lock")
                    .`is`("111"),
                bs.select<Any,Any>("a"),
                bs.constant("gg")
            ).toList()
        println("YYY: $n")

        val x = g.V().has(T.id, id).`as`("a").elementMap<Any>().toList()
        println(x)
        g.close()
    }
}
