val utf8:               String = Charsets.UTF_8.name()
val javaVersion:        Int    = property("java.version").toString().toInt()
val javaCompilerArgs:   String = property("java.compilerArgs").toString()
val projectGroup:       String = property("project.group").toString()
val projectVersion:     Any    = property("project.version")!!
val projectDescription: String = property("project.description").toString()

plugins {
    java
    alias(libs.plugins.shadow)        apply false
    alias(libs.plugins.paper.userdev) apply false
    alias(libs.plugins.paper.run)     apply false
}

subprojects {
    apply(plugin = "java")

    group       = project.group
    version     = projectVersion
    description = projectDescription

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://maven.playpro.com")
    }

    dependencies {
        compileOnly(rootProject.libs.fastutil)
        compileOnly(rootProject.libs.google.guava)
        compileOnly(rootProject.libs.google.gson)
        compileOnly(rootProject.libs.google.jsr305)
        compileOnly(rootProject.libs.jetbrains.annotations)
        compileOnly(rootProject.libs.jackson.annotations)
        compileOnly(rootProject.libs.jda)
    }

    sourceSets {
        main {
            java.srcDir(project(":common").sourceSets.main.get().java.srcDirs)
        }
    }

    tasks {
        compileJava {
            options.encoding = utf8

            options.release.set(javaVersion)
            options.compilerArgs.add(javaCompilerArgs)
        }

        jar {
            archiveBaseName.set("${rootProject.name}-${project.name}")
            destinationDirectory.set(file("$rootDir/build"))
        }

        javadoc {
            enabled = false
            options.encoding = utf8
            setDestinationDir(file("$rootDir/builds/javadoc"))
        }

        processResources {
            filteringCharset = utf8
        }
    }
}

tasks {
    jar {
        doLast {
            file("build").deleteRecursively()
        }
    }
    compileJava      { enabled = false }
    processResources { enabled = false }
    classes          { enabled = false }
    assemble         { enabled = false }
    testClasses      { enabled = false }
    test             { enabled = false }
    check            { enabled = false }
    build            { enabled = false }
}
