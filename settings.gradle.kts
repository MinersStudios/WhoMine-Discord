@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "WhoMine"

includeLib("common")
includeLib("paper")
includeLib("velocity")

includePlatform("paper")
includePlatform("velocity")

fun Settings.includeLib(libName: String) {
    val path = ":WhoLib-$libName"

    include(path)
    project(path).projectDir = File("lib/${libName}")
}

fun Settings.includePlatform(platformName: String) {
    val path = ":WhoMine-$platformName"

    include(path)
    project(path).projectDir = File("platform/$platformName")
}
