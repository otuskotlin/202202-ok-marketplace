plugins {
    kotlin("multiplatform")
}



kotlin {
    jvm {}
//    linuxX64 {}

    sourceSets {
        val coroutinesVersion: String by project

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":ok-marketplace-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api(kotlin("test-junit"))
            }
        }
    }
}
