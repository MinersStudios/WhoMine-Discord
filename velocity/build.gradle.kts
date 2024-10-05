plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":common"))

    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
}

tasks {
    shadowJar {
        destinationDirectory.set(file("$rootDir/build"))

        archiveBaseName.set(rootProject.name + "-" + project.name)
        archiveVersion.set(project.version.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}
