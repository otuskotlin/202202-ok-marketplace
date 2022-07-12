package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.junit.Test

class TmpTest {
    @Test
    fun x() {
        val cluster = Cluster.build().apply {
            addContactPoints("localhost")
            port(8182)
//            enableSsl(enableSsl)
        }.create()
        val g = AnonymousTraversalSource
            .traversal()
            .withRemote(DriverRemoteConnection.using(cluster))
        val id = g.inject("zzz").addV("Test").property("ID", "val-1").next().id()
        println("ID: $id")

        val x = g.V().elementMap<Any>().toList()
        println(x)
        g.close()
    }
}
