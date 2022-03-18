rootProject.name = "ok-202202-marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
    }
}

include("m1l1-hello-world")
include("m1l4-dsl")
include("m1l5-coroutines")
