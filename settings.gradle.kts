rootProject.name = "ok-202202-marketplace"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        val openapiVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("multiplatfrom") version kotlinVersion apply false
        id("io.kotest.multiplatform") version kotestVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        // spring
        val springBootVersion: String by settings
        val springDependencyVersion: String by settings
        val springPluginVersion: String by settings

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyVersion
        kotlin("plugin.spring") version springPluginVersion
    }
}

//include("m1l1-hello-world")
//include("m1l4-dsl")
//include("m1l5-coroutines")
//include("m1l7-multiplatform")
//include("m2l2-testing")

include("ok-marketplace-api-v1-jackson")
include("ok-marketplace-api-v2-kmp")
include("ok-marketplace-common")
include("ok-marketplace-mappers-v1")
include("ok-marketplace-mappers-v2")
include("ok-marketplace-app-spring")
include("ok-marketplace-stubs")
include("ok-marketplace-app-ktor")
include("ok-marketplace-app-ktor-native")
include("ok-marketplace-services")
include("ok-marketplace-biz")
