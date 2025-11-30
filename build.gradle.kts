plugins {
    kotlin("multiplatform") version "1.9.23" apply false
    id("com.android.library") version "8.1.0" apply false
}

group = "io.github.raystatic"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
