plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

dependencies {
    api(libs.adventure.api)
    api(libs.adventure.ansi)
    api(libs.adventure.gson)
    api(libs.adventure.legacy)
    api(libs.adventure.plain)
    api(libs.adventure.minimessage)
    api(libs.netty.buffer)
}

tasks {
    shadowJar {
        destinationDirectory.set(file("$rootDir/build"))

        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}
