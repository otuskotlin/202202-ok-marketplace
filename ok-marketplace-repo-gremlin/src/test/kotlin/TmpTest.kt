package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.apache.tinkerpop.gremlin.arcadedb.structure.io.ArcadeIoRegistry
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.driver.ser.GraphBinaryMessageSerializerV1
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.io.binary.TypeSerializerRegistry
import org.junit.Test
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as bs


//@Ignore
class TmpTest {

    @Test
    fun z() {
        val serializer = GraphBinaryMessageSerializerV1(
            TypeSerializerRegistry.Builder().addRegistry(ArcadeIoRegistry())
        )
//        val db = RemoteDatabase("localhost", 2480, "mkpl", "root", "root_root")
//        val z = db.command("gremlin", "g.V()")
//        println("ZZL: ${z.stream().map { it.toJSON() }.asSequence().joinToString("\n")}")
//        db.close()

        val cluster = Cluster.build()
            .addContactPoints("localhost")
            .port(8182)
            .credentials("root", "root_root1")
            .serializer(serializer)
//            enableSsl(enableSsl)
            .create()
        val g = traversal().withRemote(DriverRemoteConnection.using(cluster,"mkpl"))
        val userId = g
            .addV("User")
            .property("name", "Evan")
            .next()
            .id()
//        println("UserID: $userId")
        g.close()
    }

    @Test
    fun y() {
        val host = "localhost"
        val port = 8182
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(port)
//            credentials("root", "root@root")
            credentials("gremlin", "gremlin%gremlin")
//            path("/mkpl")
//            enableSsl(enableSsl)
        }.create()
        val g = traversal()
            .withRemote(DriverRemoteConnection.using(cluster))
        val x = g.V().hasLabel("Test").`as`("a")
            .project<Any>("lock", "ownerId", "z")
            .by("lock")
            .by(bs.inE("Owns").outV().id())
            .by(bs.elementMap<Vertex, Any>())
            .toList()

        println("CONTENT: ${x}")
        g.close()
    }

    @Test
    fun x() {
//        val host = ArcadeDbContainer.container.host
//        val port = ArcadeDbContainer.container.getMappedPort(8182)
        val host = "localhost"
        val port = 8182
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(port)
//            credentials("root", "root@root")
            credentials("gremlin", "gremlin%gremlin")
//            path("/mkpl")
//            enableSsl(enableSsl)
        }.create()
        val g = traversal()
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

//        val n = g
//            .V(id)
//            .`as`("a")
//            .choose(
//                bs.select<Vertex, Any>("a")
//                    .values<String>("lock")
//                    .`is`("1112"),
//                bs.select<Vertex, String>("a").drop().inject("success"),
//                bs.constant("lock-failure")
//            ).toList()
//        println("YYY: $n")

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
