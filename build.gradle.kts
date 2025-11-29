import org.gradle.api.publish.maven.MavenPublication

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}

plugins {
    kotlin("multiplatform") version "1.9.23" apply false
    id("com.android.library") version "8.1.0" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    signing
    `maven-publish`
}

group = "io.github.raystatic"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

/* -----------------------------
   SONATYPE (NEXUS) CONFIG
-------------------------------- */
nexusPublishing {
    repositories {
        sonatype {
            packageGroup.set("io.github.raystatic")
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            val user = System.getenv("MAVEN_USERNAME")
            val pass = System.getenv("MAVEN_PASSWORD")

            if (user != null && pass != null) {
                username.set(user)
                password.set(pass)
            } else {
                logger.warn("⚠️ MAVEN_USERNAME/MAVEN_PASSWORD missing — publishing will fail.")
            }
        }
    }
}

/* -----------------------------
   APPLY PUBLISHING TO ALL MODULES
-------------------------------- */
subprojects {

    afterEvaluate {

        if (!plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") &&
            !plugins.hasPlugin("com.android.library")
        ) return@afterEvaluate

        apply(plugin = "maven-publish")
        apply(plugin = "signing")

        publishing {
            publications.withType<MavenPublication> {

                groupId = rootProject.group.toString()
                version = rootProject.version.toString()

                /* Mandatory POM metadata */
                pom {
                    name.set(project.name)
                    description.set("Kotlin Multiplatform Cache Library (KCache)")
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

        /* Signing using GitHub Secrets */
        signing {
            val key = System.getenv("GPG_PRIVATE_KEY")
            val pwd = System.getenv("GPG_PRIVATE_KEY_PASSWORD")

            if (key != null && pwd != null) {
                useInMemoryPgpKeys(key, pwd)
                sign(publishing.publications)
            } else {
                println("⚠️ WARNING: GPG signing key or password is missing.")
            }
        }
    }
}
