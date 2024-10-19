import io.papermc.paperweight.userdev.ReobfArtifactConfiguration.Companion.MOJANG_PRODUCTION

plugins {
    id("whomine.library")
    id("whomine.paperweight")
}

paperweight.reobfArtifactConfiguration = MOJANG_PRODUCTION

dependencies {
    compileOnlyApi(libs.authme)
    compileOnlyApi(libs.coreprotect)
}
