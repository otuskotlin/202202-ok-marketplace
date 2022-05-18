plugins {
    kotlin("jvm")
}



dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-common"))

    testImplementation(kotlin("test-junit"))
}
