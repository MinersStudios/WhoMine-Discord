import io.papermc.paperweight.userdev.ReobfArtifactConfiguration.Companion.MOJANG_PRODUCTION

val paperVersion:       String = rootProject.property("paper.version").toString()
val author:             String = rootProject.property("project.author").toString()
val contributors:       String = rootProject.property("project.contributors").toString()
val website:            String = rootProject.property("project.website").toString()
val coreProtectVersion: String = libs.versions.coreprotect.get()
val authMeVersion:      String = libs.versions.authme.get()
val apiVersion:         String
    get() {
        val minecraftVersion = paperweight.minecraftVersion.get()
        val parts = minecraftVersion.split('.')

        return if (parts.size < 2) {
            throw IllegalStateException("Invalid Minecraft version: '$minecraftVersion'")
        } else {
            "'${parts[0]}.${parts[1]}'"
        }
    }

plugins {
    alias(libs.plugins.paper.userdev)
    alias(libs.plugins.paper.run)
    alias(libs.plugins.shadow)
}

paperweight.reobfArtifactConfiguration = MOJANG_PRODUCTION

dependencies {
    paperweight.paperDevBundle(paperVersion)

    implementation(project(":common"))
    compileOnly(libs.authme)
    compileOnly(libs.coreprotect)
}

tasks {
    processResources {
        val props = mapOf(
            "name"               to rootProject.name + "-" + project.name,
            "version"            to project.version,
            "description"        to project.description,
            "author"             to author,
            "contributors"       to contributors,
            "website"            to website,
            "apiVersion"         to apiVersion,
            "coreProtectVersion" to coreProtectVersion,
            "authMeVersion"      to libs.versions.authme.get(),
        )

        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
