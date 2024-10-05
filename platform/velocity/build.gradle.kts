plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":WhoMine-common"))

    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
}

tasks {
    shadowJar {
        destinationDirectory.set(file("$rootDir/build"))

        archiveVersion.set(project.version.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}
