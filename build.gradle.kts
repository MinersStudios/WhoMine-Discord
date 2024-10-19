plugins {
    `java-library`
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
