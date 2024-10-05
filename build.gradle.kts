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
        implementation(rootProject.libs.fastutil)
        implementation(rootProject.libs.google.guava)
        implementation(rootProject.libs.google.gson)
        implementation(rootProject.libs.google.jsr305)
        implementation(rootProject.libs.jetbrains.annotations)
        implementation(rootProject.libs.jackson.annotations)
        implementation(rootProject.libs.jda)
    }

    sourceSets {
        main {
            java.srcDir(project(":WhoMine-common").sourceSets.main.get().java.srcDirs)
        }
    }

    tasks {
        compileJava {
            options.encoding = utf8

            options.release.set(javaVersion)
            options.compilerArgs.add(javaCompilerArgs)
        }

        jar {
            destinationDirectory.set(file("$rootDir/build"))
        }

        javadoc {
            options.encoding = utf8
            setDestinationDir(file("$rootDir/build/javadoc"))
        }

        processResources {
            filteringCharset = utf8
        }
    }
}

tasks {
    jar {
        doLast {
            file("build").listFiles()?.forEach {
                if (it.name != "javadoc") {
                    it.deleteRecursively()
                }
            }
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
