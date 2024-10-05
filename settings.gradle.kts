rootProject.name = "WhoMine"

sequenceOf(
    "common",
    "paper",
    "velocity",
).forEach {
    val path = ":${rootProject.name}-$it"

    include(path)
    project(path).projectDir =
        if (it == "common") file(it)
        else file("platform/$it")
}
