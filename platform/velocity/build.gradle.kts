plugins {
    id("whomine.platform")
    id("whomine.velocitypowered")
}

dependencies {
    api(Libs.Velocity.asProject(rootProject))

    runtimeClasspath(libs.asm.api)
    runtimeClasspath(libs.asm.commons)
    runtimeClasspath(libs.fastutil)
    runtimeClasspath(libs.google.guava)
    runtimeClasspath(libs.google.gson)
    runtimeClasspath(libs.google.jsr305)
    runtimeClasspath(libs.jackson.annotations)
    runtimeClasspath(libs.jda)
    runtimeClasspath(libs.jetbrains.annotations)
    runtimeClasspath(libs.netty.buffer)
}
