import org.gradle.api.tasks.bundling.Zip

tasks.register<Zip>("bundleReleaseArtifacts") {
    group = "publishing"

    dependsOn("publishAllPublicationsToMavenLocal")

    val m2 = layout.buildDirectory.dir("localMavenRepo")

    from(m2) {
        into("")
    }

    archiveFileName.set("bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}
