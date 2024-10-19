import io.papermc.paperweight.userdev.ReobfArtifactConfiguration.Companion.MOJANG_PRODUCTION

val apiVersion: String = apiVersion(paperweight.minecraftVersion.get())

plugins {
    id("whomine.platform")
    id("whomine.paperweight")
}

paperweight.reobfArtifactConfiguration = MOJANG_PRODUCTION

dependencies {
    api(Libs.Paper.asProject(rootProject))
}

tasks {
    processResources {
        val props = mapOf(
            "name"               to project.name,
            "version"            to project.version,
            "description"        to project.description,
            "author"             to projectAuthor,
            "contributors"       to projectContributors,
            "website"            to projectWebsite,
            "apiVersion"         to apiVersion,
            "coreProtectVersion" to libs.versions.coreprotect.get(),
            "authMeVersion"      to libs.versions.authme.get()
        )

        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
