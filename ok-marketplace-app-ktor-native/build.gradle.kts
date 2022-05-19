val ktorVersion: String by project

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        // Windows is currently not supported
        // Other supported targets are listed here: https://ktor.io/docs/native-server.html#targets
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "ru.otus.otuskotlin.marketplace.main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":ok-marketplace-common"))
                implementation(project(":ok-marketplace-api-v2-kmp"))
                implementation(project(":ok-marketplace-mappers-v2"))

                implementation(project(":ok-marketplace-services"))
                implementation(project(":ok-marketplace-stubs"))
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")

                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
