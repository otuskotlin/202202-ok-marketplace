plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    // transport models
    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-stubs"))
}
