plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}
val kotestVersion = "4.6.1"

//java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    val kotestVersion: String by project
    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
    implementation("org.springframework.boot:spring-boot-starter-web") // Controller, Service, etc..
    implementation("org.springframework.boot:spring-boot-starter-websocket") // Controller, Service, etc..
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // for spring-boot app

    // transport models
    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-api-v2-kmp"))
    implementation(project(":ok-marketplace-mappers-v1"))

    // logging
    implementation(project(":ok-marketplace-logging"))
    implementation(project(":ok-marketplace-logs-mapper"))
    implementation(project(":ok-marketplace-api-logs"))

    // v2 api
    implementation(project(":ok-marketplace-mappers-v2"))

    // Services
    implementation(project(":ok-marketplace-services"))

    // Stubs
    implementation(project(":ok-marketplace-stubs"))

    // Repositories
    implementation(project(":ok-marketplace-repo-inmemory"))

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
    testImplementation("com.ninja-squad:springmockk:3.0.1") // mockking beans
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions {
//        freeCompilerArgs = listOf("-Xjsr305=strict")
//        jvmTarget = "11"
//    }
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
