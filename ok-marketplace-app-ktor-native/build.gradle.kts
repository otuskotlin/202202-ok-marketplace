import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile

val ktorVersion: String by project

plugins {
    kotlin("multiplatform")
    id("com.bmuschko.docker-remote-api")
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
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":ok-marketplace-common"))
                implementation(project(":ok-marketplace-api-v2-kmp"))
                implementation(project(":ok-marketplace-mappers-v2"))

                implementation(project(":ok-marketplace-services"))
                implementation(project(":ok-marketplace-app-ktor-common"))
                implementation(project(":ok-marketplace-repo-inmemory"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-cio:$ktorVersion")

                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val nativeTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks {
    val linkReleaseExecutableNative by getting(org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink::class) {

    }
    val dockerDockerfile by creating(Dockerfile::class) {
        group = "docker"
        dependsOn(linkReleaseExecutableNative)
        from("ubuntu:22.04")
        doFirst {
            copy {
                from(linkReleaseExecutableNative.binary.outputFile)
                into("$buildDir/docker")
                rename(".*", "app")
            }
        }
        copyFile("app", "/app")
        entryPoint("/app")
    }
    @Suppress("UNUSED_VARIABLE")
    val dockerBuildImage by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfile)
        images.add("${project.name}:${project.version}")
    }
}
