import org.gradle.kotlin.dsl.invoke

plugins {
    id("whomine.base")
    id("com.gradleup.shadow")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
