plugins {
    kotlin("multiplatform")
}



kotlin {
    jvm {}
//    linuxX64 {}

    sourceSets {
        val arcadeDbVersion: String by project
        val tinkerpopVersion: String by project
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project
        val testContainersVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":ok-marketplace-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("com.benasher44:uuid:$kmpUUIDVersion")

            }
        }
        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":ok-marketplace-repo-test"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.apache.tinkerpop:gremlin-driver:$tinkerpopVersion")
                implementation("com.arcadedb:arcadedb-engine:$arcadeDbVersion")
                implementation("com.arcadedb:arcadedb-network:$arcadeDbVersion")
                implementation("com.arcadedb:arcadedb-gremlin:$arcadeDbVersion")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.testcontainers:testcontainers:$testContainersVersion")
            }
        }
    }
}
