plugins {
    kotlin("multiplatform") version "1.9.23" apply false
    id("com.android.library") version "8.1.0" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
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
   NEW MAVEN CENTRAL PUBLISHER
-------------------------------- */
nexusPublishing {
    packageGroup.set("io.github.raystatic")

    repositories {
        create("mavenCentral") {
            // NEW Publisher API (no staging)
            nexusUrl.set(uri("https://central.sonatype.com/api/v1/publisher/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/api/v1/publisher/"))

            username.set(System.getenv("MAVEN_CENTRAL_TOKEN_USERNAME"))
            password.set(System.getenv("MAVEN_CENTRAL_TOKEN_PASSWORD"))
        }
    }
}
