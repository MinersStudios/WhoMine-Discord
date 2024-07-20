plugins {
    java
    alias(libs.plugins.paper.userdev)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group =       "com.minersstudios"
version =     "1.0.0"
description = "A Minecraft plugin for WhoMine"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.playpro.com")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jackson.annotations)
    compileOnly(libs.jda)
    compileOnly(libs.authme)
    compileOnly(libs.coreprotect)
}

sourceSets {
    test {
        java {
            srcDirs("src/main/test/java")
        }
    }
}

tasks {
    val utf8 = Charsets.UTF_8.name()

    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = utf8

        options.release.set(17)
        options.compilerArgs.add("-Xlint:deprecation")
    }

    javadoc {
        options.encoding = utf8
    }

    processResources {
        filteringCharset = utf8
        val props = mapOf(
                "name"               to rootProject.name,
                "version"            to rootProject.version,
                "description"        to "A Minecraft plugin for WhoMine",
                "author"             to "MinersStudios",
                "contributors"       to "p0loskun, PackmanDude",
                "website"            to "https://whomine.net",
                "apiVersion"         to "'1.20'",
                "coreProtectVersion" to libs.versions.coreprotect.get(),
                "authMeVersion"      to libs.versions.authme.get(),
        )

        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
