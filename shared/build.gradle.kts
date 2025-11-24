plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.9.23"
    `maven-publish`
    signing
}

group = "com.raystatic.filecache"
version = "1.0.0"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
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
        val iosMain by creating {
            dependsOn(commonMain)
        }
        val jvmMain by getting
    }
}

android {
    namespace = "com.raystatic.filecache"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 21
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("KMP File Cache")
            description.set("A Kotlin Multiplatform file-based cache library with LRU eviction and TTL support")
            url.set("https://github.com/raystatic/kmp-file-cache")
            
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            
            developers {
                developer {
                    id.set("raystatic")
                    name.set("RayStatic")
                    email.set("info@raystatic.com")
                }
            }
            
            scm {
                connection.set("scm:git:git://github.com/raystatic/kmp-file-cache.git")
                developerConnection.set("scm:git:ssh://github.com:raystatic/kmp-file-cache.git")
                url.set("https://github.com/raystatic/kmp-file-cache/tree/main")
            }
        }
    }
}

signing {
    val signingKeyId: String? = project.findProperty("signing.keyId") as String?
    val signingPassword: String? = project.findProperty("signing.password") as String?
    val signingKey: String? = project.findProperty("signing.secretKey") as String?
    
    if (signingKeyId != null && signingKey != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}

