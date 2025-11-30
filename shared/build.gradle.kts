plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.9.23"
    `maven-publish`
    signing
}

kotlin {
    androidTarget()

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            }
        }
        val androidMain by getting
        val iosMain by creating { dependsOn(commonMain) }
        val jvmMain by getting
    }
}

// JVM TARGET FIX — Kotlin 1.9.x + AGP 8.x compatible
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

android {
    namespace = "io.github.raystatic.kcache"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("KCache")
            description.set("Kotlin Multiplatform File Cache with LRU and TTL")
            url.set("https://github.com/raystatic/KCache")

            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }

            developers {
                developer {
                    id.set("raystatic")
                    name.set("Rahul Ray")
                }
            }

            scm {
                url.set("https://github.com/raystatic/KCache")
                connection.set("scm:git:https://github.com/raystatic/KCache.git")
                developerConnection.set("scm:git:ssh://github.com:raystatic/KCache.git")
            }
        }
    }
}

signing {
    val key = System.getenv("GPG_PRIVATE_KEY")
    val pwd = System.getenv("GPG_PRIVATE_KEY_PASSWORD")

    if (key != null && pwd != null) {
        useInMemoryPgpKeys(key, pwd)
        sign(publishing.publications)
    } else {
        println("⚠️ GPG private key not configured — signing skipped")
    }
}

apply(from = rootProject.file("shared/bundleArtifacts.gradle.kts"))