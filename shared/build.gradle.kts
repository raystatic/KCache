plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.9.23"
    `maven-publish`
    signing
}

kotlin {
    android {
        publishLibraryVariants("release")
    }

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

android {
    namespace = "io.github.raystatic.kcache"
    compileSdk = 34

    defaultConfig { minSdk = 21 }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

/* ---------- PUBLISHING METADATA ---------- */
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
                developerConnection.set("scm:git:https://github.com/raystatic/KCache.git")
            }
        }
    }
}

/* ---------- GPG SIGNING ---------- */
signing {
    val key = System.getenv("GPG_PRIVATE_KEY")
    val pass = System.getenv("GPG_PRIVATE_KEY_PASSWORD")

    if (!key.isNullOrBlank() && !pass.isNullOrBlank()) {
        useInMemoryPgpKeys(key, pass)
        sign(publishing.publications)
    } else {
        println("⚠️ GPG key missing, signing skipped.")
    }
}

/* ---------- CREATE BUNDLE FOR NEW API ---------- */
tasks.register<Zip>("createCentralBundle") {

    dependsOn("publishToMavenLocal")

    archiveFileName.set("bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    from("${rootProject.layout.buildDirectory}/localMaven") {
        include("io/github/raystatic/**")
    }
}
