plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    val coroutinesVersion: String by project
    val cassandraDriverVersion: String by project
    val testContainersVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":ok-marketplace-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")

    implementation("com.datastax.oss:java-driver-core:$cassandraDriverVersion")
    implementation("com.datastax.oss:java-driver-query-builder:$cassandraDriverVersion")

    kapt("com.datastax.oss:java-driver-mapper-processor:$cassandraDriverVersion")
    implementation("com.datastax.oss:java-driver-mapper-runtime:$cassandraDriverVersion")

    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    testImplementation(project(":ok-marketplace-repo-test"))
    testImplementation("org.testcontainers:cassandra:$testContainersVersion")
}
