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
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

// Configure Nexus Publish Plugin
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            
            val ossrhUsername: String? = project.findProperty("ossrhUsername") as String?
            val ossrhPassword: String? = project.findProperty("ossrhPassword") as String?
            
            if (ossrhUsername != null && ossrhPassword != null) {
                username.set(ossrhUsername)
                password.set(ossrhPassword)
            }
        }
    }
    
    // Automatically close and release staging repository
    transitionCheckOptions {
        maxRetries.set(40)
        delayBetween.set(java.time.Duration.ofSeconds(10))
    }
}
