plugins {
    kotlin("jvm")
}

dependencies {

    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":ok-marketplace-common"))

    api(kotlin("test-junit"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}
