package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
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
        val userId = g
            .addV("User")
            .property("name", "Ivan")
            .next()
            .id()
        println("UserID: $userId")

        val id = g
            .addV("Test")
            .`as`("a")
            .property("lock", "111")
            .addE("Owns")
            .from(bs.V<Vertex>(userId))
            .select<Vertex>("a")
            .next()
            .id()
        println("ID: $id")

        val owner = g
            .V(userId)
            .outE("Owns")
            .where(bs.inV().id().`is`(id))
            .toList()
        println("OWNER: $owner")

        val n = g
            .V(id)
            .`as`("a")
            .choose(
                bs.select<Vertex, Any>("a")
                    .values<String>("lock")
                    .`is`("1112"),
                bs.select<Vertex, String>("a").drop().inject("success"),
                bs.constant("lock-failure")
            ).toList()
        println("YYY: $n")

//        val x = g.V(id).`as`("a")
//            .union(
//                bs.select<Vertex, Edge>("a")
//                    .inE("User")
//                    .outV()
//                    .V(),
//                bs.select<Vertex, Vertex>("a")
//            )
//            .elementMap<Any>()
//            .toList()
//        println("CONTENT: ${x}")
        g.close()
    }
}
