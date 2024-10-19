import org.gradle.kotlin.dsl.invoke

plugins {
    id("whomine.shadow")
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        destinationDirectory.set(file("$rootDir/build/platform"))

        archiveClassifier.set("")
    }
}
